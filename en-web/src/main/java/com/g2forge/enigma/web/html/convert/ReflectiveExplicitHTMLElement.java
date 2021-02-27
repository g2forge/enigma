package com.g2forge.enigma.web.html.convert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.java.type.implementations.ReflectionException;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IThrowFunction1;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.reflection.object.HReflection;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

class ReflectiveExplicitHTMLElement implements IExplicitHTMLRenderable {
	@Data
	protected static class Property<T, F> {
		protected final String name;

		protected final HTMLField field;

		protected final IFunction1<? super T, ? extends F> accessor;

		protected final Type type;

		public String getNameClose() {
			return "";
		}

		public String getNameOpen() {
			return "";
		}

		public boolean isProperty() {
			return getField().property();
		}
	}

	@HTMLField
	protected static final Object DUMMY = null;

	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private static final HTMLField defaultHTMLField = computeDefaultHTMLField();

	protected static HTMLField computeDefaultHTMLField() {
		try {
			return ReflectiveExplicitHTMLElement.class.getDeclaredField("DUMMY").getAnnotation(HTMLField.class);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeReflectionException(e);
		}
	}

	protected final IReflectiveHTMLElement element;

	protected final String tag;

	protected final HTMLTag.Pretty pretty;

	protected final List<ReflectiveExplicitHTMLElement.Property<IReflectiveHTMLElement, ?>> properties;

	protected final List<ReflectiveExplicitHTMLElement.Property<IReflectiveHTMLElement, ?>> content;

	public ReflectiveExplicitHTMLElement(IReflectiveHTMLElement element) {
		this.element = element;

		{ // Compute the tag & pretty control
			final HTMLTag annotation = element.getClass().getAnnotation(HTMLTag.class);
			if (annotation == null) {
				this.tag = element.getClass().getSimpleName().toLowerCase();
				this.pretty = HTMLTag.Pretty.Block;
			} else {
				final boolean explicit = !"".equals(annotation.value());
				final boolean generator = annotation.generator() != IHTMLTagGenerator.class;
				if (explicit && generator) throw new IllegalArgumentException();
				else if (explicit) this.tag = annotation.value();
				else if (generator) {
					try {
						this.tag = annotation.generator().getDeclaredConstructor().newInstance().apply(element);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
						throw new ReflectionException(exception);
					}
				} else this.tag = element.getClass().getSimpleName().toLowerCase();

				this.pretty = annotation.pretty();
			}
		}

		final Map<Boolean, List<ReflectiveExplicitHTMLElement.Property<IReflectiveHTMLElement, ?>>> map = HReflection.toReflection(element).getFields(JavaScope.Inherited, null).filter(field -> field.getAnnotations().getAnnotation(HTMLIgnore.class) == null).map(field -> {
			final HTMLField annotation = field.getAnnotations().getAnnotation(HTMLField.class);
			final IFunction1<IReflectiveHTMLElement, Object> accessor = IThrowFunction1.<IReflectiveHTMLElement, Object, Throwable>create(object -> field.getAccessor(object).get0()).wrap(RuntimeReflectionException::new);
			return new ReflectiveExplicitHTMLElement.Property<>(field.getType().getName(), annotation == null ? getDefaultHTMLField() : annotation, accessor, field.getType().getJavaMember().getGenericType());
		}).collect(Collectors.groupingBy(Property::isProperty));
		this.properties = map.get(true);
		this.content = map.get(false);
	}

	@Override
	public void render(IHTMLRenderContext context) {
		context.append('<').append(tag);

		if (properties != null) properties.forEach(property -> {
			final Object value = property.getAccessor().apply(element);
			if (!property.getField().skipNull() || (value != null)) context.append(' ').append(property.getNameOpen()).append(property.getName()).append(property.getNameClose()).append("=\"").render(value, property.getType()).append("\"");
		});

		// Contents
		if ((content != null) && !content.isEmpty()) {
			try (final ICloseable closeTag = context.openTag(tag, pretty == HTMLTag.Pretty.Inline)) {
				try (final ICloseable closeIndent = (pretty == HTMLTag.Pretty.Block) ? context.indent() : () -> {}) {
					for (ReflectiveExplicitHTMLElement.Property<IReflectiveHTMLElement, ?> property : content) {
						final Object value = property.getAccessor().apply(element);
						if (!property.getField().skipNull() || (value != null)) {
							if (value instanceof Collection) {
								for (Object child : ((Collection<?>) value)) {
									context.render(child, null);
									if (pretty != HTMLTag.Pretty.Inline) context.newline();
								}
							} else {
								context.render(value, property.getType());
								if (pretty != HTMLTag.Pretty.Inline) context.newline();
							}
						}
					}
				}
			}
		} else context.append("/>");
	}
}