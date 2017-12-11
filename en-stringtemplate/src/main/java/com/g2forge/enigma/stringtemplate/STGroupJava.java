package com.g2forge.enigma.stringtemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.antlr.runtime.ANTLRInputStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STAdvanced;
import org.stringtemplate.v4.STAttributeGenerator;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.compiler.CompiledST;
import org.stringtemplate.v4.misc.ErrorBuffer;
import org.stringtemplate.v4.misc.ErrorManager;
import org.stringtemplate.v4.misc.STMessage;

import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.core.helpers.StreamHelpers;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.object.ReflectionHelpers;
import com.g2forge.alexandria.reflection.record.v2.IPropertyType;
import com.g2forge.alexandria.reflection.record.v2.IRecordType;

import lombok.Data;

/**
 * @see #render(Object)
 */
public class STGroupJava extends STGroup {
	@Data
	protected static class ProxySTErrorListener implements STErrorListener {
		protected final Collection<STErrorListener> delegates;

		@Override
		public void compileTimeError(STMessage msg) {
			proxy(STErrorListener::compileTimeError, msg);
		}

		@Override
		public void internalError(STMessage msg) {
			proxy(STErrorListener::internalError, msg);
		}

		@Override
		public void IOError(STMessage msg) {
			proxy(STErrorListener::IOError, msg);
		}

		protected void proxy(BiConsumer<? super STErrorListener, ? super STMessage> method, final STMessage msg) {
			delegates.forEach(delegate -> method.accept(delegate, msg));
		}

		@Override
		public void runTimeError(STMessage msg) {
			proxy(STErrorListener::runTimeError, msg);
		}
	}

	protected final Map<String, Class<?>> types = new HashMap<>();

	protected final String lineSeparator;

	protected final Function<? super Object, ? extends Object> adapter;

	protected final Function<? super Class<?>, ? extends IRecordType> recordFunction;

	public STGroupJava(String encoding, char delimiterStartChar, char delimiterStopChar, String lineSeparator, Function<? super Object, ? extends Object> adapter, Function<? super Class<?>, ? extends IRecordType> recordFunction) {
		super(delimiterStartChar, delimiterStopChar);
		this.encoding = encoding;
		this.registerRenderer(Object.class, new JavaStringRenderer(this));
		this.lineSeparator = lineSeparator;
		this.adapter = adapter == null ? Function.identity() : adapter;
		this.recordFunction = recordFunction;
	}

	public ST createStringTemplate(CompiledST impl) {
		return new STAdvanced(super.createStringTemplate(impl), lineSeparator);
	}

	public ST getInstanceOf(Class<?> type) {
		if (!type.isEnum() && Enum.class.isAssignableFrom(type)) type = type.getSuperclass();
		final String name = getTemplateName(type);

		final Class<?> prior = types.get(name);
		if (prior == null) types.put("/" + type.getSimpleName(), type);
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
		if (type == null) throw new NullPointerException("Failed to load template \"" + name + "\", there was an internal error!");
		final String fileName = type.getSimpleName() + ".st", templateName = type.getSimpleName();

		final InputStream stream;
		final Optional<IJavaFieldReflection<?, ?>> template = ReflectionHelpers.toReflection(type).getFields(JavaScope.Static, null).filter(field -> "TEMPLATE".equals(field.getType().getName())).collect(StreamHelpers.toOptional());
		if ((template == null) || !template.isPresent()) stream = type.getResourceAsStream(fileName);
		else {
			try {
				final String arguments = recordFunction.apply(type).getProperties().stream().map(IPropertyType::getName).collect(Collectors.joining(", "));
				final IJavaFieldReflection<?, ?> field = template.get();
				final String string = field.getAccessor(null).get0().toString();
				final String complete = templateName + "(" + arguments + ") ::= <<\n" + string + "\n>>";
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

		final ErrorBuffer buffer = new ErrorBuffer();
		final ErrorManager prior = this.errMgr;
		try {
			this.errMgr = new ErrorManager(new ProxySTErrorListener(Arrays.asList(buffer, prior.listener)));
			return loadTemplateFile("/", fileName, fs);
		} finally {
			this.errMgr = prior;
			if (!buffer.errors.isEmpty()) { throw new RuntimeException("One or more ST errors while loading the template \"" + templateName + "\"!"); }
		}
	}

	public String render(Object raw) {
		final Object adapted = adapter.apply(raw);

		final Class<? extends Object> type = adapted.getClass();
		final ST retVal = getInstanceOf(type);
		if (retVal == null) throw new NoTemplateException("Template could not be found in either a file or field for " + type);
		recordFunction.apply(type).getProperties().forEach(property -> retVal.add(property.getName(), (STAttributeGenerator) () -> property.getValue(adapted)));
		return retVal.render();
	}
}
