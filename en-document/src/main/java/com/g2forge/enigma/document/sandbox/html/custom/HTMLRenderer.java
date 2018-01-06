package com.g2forge.enigma.document.sandbox.html.custom;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IThrowFunction1;
import com.g2forge.alexandria.reflection.object.HReflection;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

public class HTMLRenderer {
	protected enum ContentState {
		Empty,
		Prepped,
		Written;
	}

	@Data
	protected static class HTMLRenderContext implements IHTMLRenderContext {
		protected final StringBuilder builder;

		protected final String newline;

		protected final IFunction2<? super Object, ? super Type, ? extends IExplicitHTMLElement> explicit;
	}

	protected static class ReflectiveExplicitHTMLElement implements IExplicitHTMLElement {
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

		@Data
		protected static class Property<T, F> {
			protected final String name;

			protected final HTMLField field;

			protected final IFunction1<? super T, ? extends F> accessor;

			protected final Type type;

			public boolean isProperty() {
				return getField().property();
			}
		}

		protected final IReflectiveHTMLElement element;

		protected final String tag;

		protected final List<Property<IReflectiveHTMLElement, ?>> properties;

		protected final List<Property<IReflectiveHTMLElement, ?>> content;

		public ReflectiveExplicitHTMLElement(IReflectiveHTMLElement element) {
			this.element = element;
			this.tag = element.getClass().getSimpleName().toLowerCase();

			final Map<Boolean, List<Property<IReflectiveHTMLElement, ?>>> map = HReflection.toReflection(element).getFields(JavaScope.Inherited, null).map(field -> {
				final HTMLField annotation = field.getAnnotations().getAnnotation(HTMLField.class);
				final IFunction1<IReflectiveHTMLElement, Object> accessor = IThrowFunction1.<IReflectiveHTMLElement, Object, Throwable>create(object -> field.getAccessor(object).get0()).wrap(RuntimeReflectionException::new);
				return new Property<>(field.getType().getName(), annotation == null ? getDefaultHTMLField() : annotation, accessor, field.getType().getJavaMember().getGenericType());
			}).collect(Collectors.groupingBy(Property::isProperty));
			this.properties = map.get(true);
			this.content = map.get(false);
		}

		@Override
		public void render(IHTMLRenderContext context) {
			final StringBuilder builder = context.getBuilder();
			builder.append('<').append(tag);

			properties.forEach(property -> {
				final Object value = property.getAccessor().apply(element);
				if (!property.getField().skipNull() || (value != null)) builder.append(" \"").append(property.getName()).append("\"=\"").append(value).append("\"");
			});

			{ // Contents
				ContentState state = ContentState.Empty;
				final IFunction2<Object, ? super Type, ? extends IExplicitHTMLElement> toExplicit = context.getExplicit();
				for (Property<IReflectiveHTMLElement, ?> property : content) {
					final Object value = property.getAccessor().apply(element);
					if (!property.getField().skipNull() || (value != null)) {
						if (state == ContentState.Empty) {
							builder.append('>');
							state = ContentState.Prepped;
						}
						final int length = builder.length();

						final IExplicitHTMLElement explicit = toExplicit.apply(value, property.getType());
						explicit.render(context);

						if (length != builder.length()) state = ContentState.Written;
					}
				}

				switch (state) {
					case Empty:
						// No content was even found, so just wrap up
						builder.append("/>");
						break;
					case Prepped:
						// We found content, but it didn't actually write anything to change the ">" at the end of the buffer to a "/>"
						builder.replace(builder.length() - 1, builder.length(), "/").append(">");
						break;
					case Written:
						// Content was written so actually write the closing tag
						builder.append("</").append(tag).append('>');
						break;
				}
			}
		}
	}

	public String render(Object element) {
		final IExplicitHTMLElement explicit = toExplicit(element, null);

		final StringBuilder retVal = new StringBuilder();
		explicit.render(new HTMLRenderContext(retVal, "\n", this::toExplicit));
		return retVal.toString();
	}

	protected IExplicitHTMLElement toExplicit(final Object element, Type type) {
		if (element instanceof IExplicitHTMLElement) return (IExplicitHTMLElement) element;
		if (element instanceof IReflectiveHTMLElement) return new ReflectiveExplicitHTMLElement((IReflectiveHTMLElement) element);
		if (element instanceof Collection) return context -> ((Collection<?>) element).stream().forEach(child -> context.getExplicit().apply(child, null).render(context));
		if ((element instanceof String) || (element instanceof Boolean) || (element instanceof Integer) || (element instanceof Long) || (element instanceof Float) || (element instanceof Double)) return context -> context.getBuilder().append(element);
		throw new IllegalArgumentException("Vale \"" + element + "\" with class " + element.getClass() + " could not be rendered to HTML!");
	}
}
