package com.g2forge.enigma.stringtemplate;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.antlr.runtime.ANTLRInputStream;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STWriter;
import org.stringtemplate.v4.compiler.CompiledST;

import com.g2forge.alexandria.java.StreamHelpers;
import com.g2forge.alexandria.java.reflection.JavaScope;
import com.g2forge.alexandria.java.reflection.ReflectionHelpers;

/**
 * @see #render(Object)
 */
public class STGroupJava extends STGroup {
	protected static class STJava extends ST {
		protected final String lineSeparator;

		public STJava(ST proto, String lineSeparator) {
			super(proto);
			this.lineSeparator = lineSeparator;
		}

		protected STWriter createAutoIndentWriter(int lineWidth, final Writer out) {
			final STWriter retVal = new AutoIndentWriter(out, lineSeparator);
			retVal.setLineWidth(lineWidth);
			return retVal;
		}

		public String render(Locale locale, int lineWidth) {
			final StringWriter retVal = new StringWriter();
			write(createAutoIndentWriter(lineWidth, retVal), locale);
			return retVal.toString();
		}

		public int write(File outputFile, STErrorListener listener, String encoding, Locale locale, int lineWidth) throws IOException {
			try (final Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), encoding))) {
				return write(createAutoIndentWriter(lineWidth, bufferedWriter), locale, listener);
			}
		}
	}

	protected final Map<String, Class<?>> types = new HashMap<>();

	protected final String lineSeparator;

	public STGroupJava(String encoding, char delimiterStartChar, char delimiterStopChar, String lineSeparator) {
		super(delimiterStartChar, delimiterStopChar);
		this.encoding = encoding;
		this.registerRenderer(Object.class, new JavaStringRenderer(this));
		this.lineSeparator = lineSeparator;
	}

	public ST createStringTemplate(CompiledST impl) {
		return new STJava(super.createStringTemplate(impl), lineSeparator);
	}

	public ST getInstanceOf(Class<?> type) {
		final String name = getTemplateName(type);

		final Class<?> prior = types.get(name);
		if (prior == null) types.put(type.getSimpleName(), type);
		else if (!prior.equals(type)) throw new Error("Cannot process two classes with the same simple name");

		return this.getInstanceOf(type.getSimpleName());
	}

	protected String getTemplateName(Class<?> type) {
		return type.getSimpleName();
	}

	protected Class<?> getTemplateType(String name) {
		return types.get(name);
	}

	@Override
	protected CompiledST load(String name) {
		final Class<?> type = getTemplateType(name);
		final String fileName = type.getSimpleName() + ".st";

		final InputStream stream;

		final Optional<Field> template = ReflectionHelpers.getFields(type, JavaScope.Static, null).filter(field -> "TEMPLATE".equals(field.getName())).collect(StreamHelpers.toOptional());
		if (template == null) stream = type.getResourceAsStream(fileName);
		else {
			try {
				final String arguments = ReflectionHelpers.getFields(type, JavaScope.Inherited, null).map(Field::getName).collect(Collectors.joining(", "));
				final Field field = template.get();
				field.setAccessible(true);
				final String string = field.get(null).toString();
				final String complete = name + "(" + arguments + ") ::= <<\n" + string + "\n>>";
				stream = new ByteArrayInputStream(complete.getBytes(encoding));
			} catch (Throwable throwable) {
				return null;
			}
		}

		final ANTLRInputStream fs;
		try {
			fs = new ANTLRInputStream(stream, encoding);
			fs.name = fileName;
		} catch (IOException exception) {
			return null;
		}
		return loadTemplateFile("", fileName, fs);
	}

	public String render(Object object) {
		final Class<? extends Object> type = object.getClass();
		final ST retVal = getInstanceOf(type);
		if (retVal == null) throw new IllegalArgumentException("Template could not be found in either a file or field for " + type);
		ReflectionHelpers.getFields(type, JavaScope.Inherited, null).forEach(field -> {
			field.setAccessible(true);
			final Object value;
			try {
				value = field.get(object);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			retVal.add(field.getName(), value);
		});
		return retVal.render();
	}
}
