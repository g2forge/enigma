package com.g2forge.enigma.stringtemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.antlr.runtime.ANTLRInputStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.compiler.CompiledST;

import com.g2forge.alexandria.java.StreamHelpers;
import com.g2forge.alexandria.java.associative.cache.Cache;
import com.g2forge.alexandria.java.associative.cache.LRUCacheEvictionPolicy;
import com.g2forge.alexandria.java.reflection.JavaScope;
import com.g2forge.alexandria.java.reflection.ReflectionHelpers;
import com.g2forge.enigma.stringtemplate.record.IProperty;
import com.g2forge.enigma.stringtemplate.record.Record;

/**
 * @see #render(Object)
 */
public class STGroupJava extends STGroup {
	protected final Map<String, Class<?>> types = new HashMap<>();

	protected final String lineSeparator;

	protected final Cache<Class<?>, Record> recordCache = new Cache<>(Record::new, new LRUCacheEvictionPolicy<>(30));

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
		if (!type.isEnum() && Enum.class.isAssignableFrom(type)) type = type.getSuperclass();
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
		if ((template == null) || !template.isPresent()) stream = type.getResourceAsStream(fileName);
		else {
			try {
				final String arguments = recordCache.apply(type).getProperties().stream().map(IProperty::getName).collect(Collectors.joining(", "));
				final Field field = template.get();
				field.setAccessible(true);
				final String string = field.get(null).toString();
				final String complete = name + "(" + arguments + ") ::= <<\n" + string + "\n>>";
				stream = new ByteArrayInputStream(complete.getBytes(encoding));
			} catch (Throwable throwable) {
				return null;
			}
		}
		if (stream == null) return null;

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
		if (retVal == null) throw new NoTemplateException("Template could not be found in either a file or field for " + type);
		recordCache.apply(type).getProperties().forEach(property -> retVal.add(property.getName(), property.getValue(object)));
		return retVal.render();
	}
}
