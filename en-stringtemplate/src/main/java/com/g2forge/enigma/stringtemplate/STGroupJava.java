package com.g2forge.enigma.stringtemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.runtime.ANTLRInputStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.compiler.CompiledST;

import com.g2forge.alexandria.java.StreamHelpers;

/**
 * @see #render(Object)
 */
public class STGroupJava extends STGroup {
	protected final Map<String, Class<?>> types = new HashMap<>();

	public STGroupJava(String encoding, char delimiterStartChar, char delimiterStopChar) {
		super(delimiterStartChar, delimiterStopChar);
		this.encoding = encoding;
		this.registerRenderer(Object.class, new JavaStringRenderer(this));
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

		final Optional<Field> template = Stream.of(type.getDeclaredFields()).filter(field -> "TEMPLATE".equals(field.getName())).filter(field -> Modifier.isStatic(field.getModifiers())).collect(StreamHelpers.toOptional());
		if (template == null) stream = type.getResourceAsStream(fileName);
		else {
			try {
				final String arguments = Stream.of(type.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).map(Field::getName).collect(Collectors.joining(", "));
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
		Stream.of(type.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).forEach(field -> {
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
