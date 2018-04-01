package com.g2forge.enigma.web.html.convert;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.generic.type.java.type.implementations.ReflectionException;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.core.error.UnreachableCodeError;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IThrowFunction1;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch1;
import com.g2forge.alexandria.reflection.object.HReflection;
import com.g2forge.enigma.web.css.convert.CSSRenderer;
import com.g2forge.enigma.web.css.convert.ICSSRenderable;

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
		protected static CSSRenderer css = new CSSRenderer();

		protected static final IFunction1<Object, IExplicitHTMLElement> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitHTMLElement>().with(builder -> {
			builder.add(IExplicitHTMLElement.class, IFunction1.identity());
			builder.add(ICSSRenderable.class, e -> context -> context.getBuilder().append(css.render(e)));
			builder.add(IReflectiveHTMLElement.class, e -> new ReflectiveExplicitHTMLElement(e));
			builder.add(Collection.class, e -> {
				final Collection<?> c = e;
				return context -> c.stream().forEach(child -> context.toExplicit(child, null).render(context));
			});

			builder.add(String.class, e -> context -> context.getBuilder().append(e));
			builder.add(Boolean.class, e -> context -> context.getBuilder().append(e));
			builder.add(Integer.class, e -> context -> context.getBuilder().append(e));
			builder.add(Long.class, e -> context -> context.getBuilder().append(e));
			builder.add(Float.class, e -> context -> context.getBuilder().append(e));
			builder.add(Double.class, e -> context -> context.getBuilder().append(e));
		}).build();

		protected final StringBuilder builder;

		protected final String newline;

		@Override
		public IExplicitHTMLElement toExplicit(final Object element, Type type) {
			return toExplicit.apply(element);
		}
	}

	protected static class ReflectiveExplicitHTMLElement implements IExplicitHTMLElement {
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

		protected final List<Property<IReflectiveHTMLElement, ?>> properties;

		protected final List<Property<IReflectiveHTMLElement, ?>> content;

		public ReflectiveExplicitHTMLElement(IReflectiveHTMLElement element) {
			this.element = element;

			{ // Compute the tag
				final HTMLTag annotation = element.getClass().getAnnotation(HTMLTag.class);
				if (annotation == null) this.tag = element.getClass().getSimpleName().toLowerCase();
				else {
					final boolean explicit = !"".equals(annotation.value());
					final boolean generator = annotation.generator() != IHTMLTagGenerator.class;
					if (explicit == generator) throw new IllegalArgumentException();
					else if (explicit) this.tag = annotation.value();
					else if (generator) try {
						this.tag = annotation.generator().newInstance().apply(element);
					} catch (InstantiationException | IllegalAccessException exception) {
						throw new ReflectionException(exception);
					}
					else throw new UnreachableCodeError();
				}
			}

			final Map<Boolean, List<Property<IReflectiveHTMLElement, ?>>> map = HReflection.toReflection(element).getFields(JavaScope.Inherited, null).filter(field -> field.getAnnotations().getAnnotation(HTMLIgnore.class) == null).map(field -> {
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

			if (properties != null) properties.forEach(property -> {
				final Object value = property.getAccessor().apply(element);
				if (!property.getField().skipNull() || (value != null)) {
					builder.append(' ').append(property.getNameOpen()).append(property.getName()).append(property.getNameClose()).append("=\"");
					context.toExplicit(value, property.getType()).render(context);
					builder.append("\"");
				}
			});

			{ // Contents
				ContentState state = ContentState.Empty;
				if (content != null) for (Property<IReflectiveHTMLElement, ?> property : content) {
					final Object value = property.getAccessor().apply(element);
					if (!property.getField().skipNull() || (value != null)) {
						if (state == ContentState.Empty) {
							builder.append('>');
							state = ContentState.Prepped;
						}
						final int length = builder.length();

						context.toExplicit(value, property.getType()).render(context);
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
		final StringBuilder retVal = new StringBuilder();
		final HTMLRenderContext context = new HTMLRenderContext(retVal, "\n");
		context.toExplicit(element, null).render(context);
		return retVal.toString();
	}

}
