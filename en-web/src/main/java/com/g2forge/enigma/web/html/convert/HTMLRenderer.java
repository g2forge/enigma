package com.g2forge.enigma.web.html.convert;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.java.type.implementations.ReflectionException;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IThrowFunction1;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
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

			builder.add(String.class, e -> context -> context.getBuilder().append(e));
			builder.add(Boolean.class, e -> context -> context.getBuilder().append(e));
			builder.add(Integer.class, e -> context -> context.getBuilder().append(e));
			builder.add(Long.class, e -> context -> context.getBuilder().append(e));
			builder.add(Float.class, e -> context -> context.getBuilder().append(e));
			builder.add(Double.class, e -> context -> context.getBuilder().append(e));
		}).build();

		protected final StringBuilder builder;

		protected int indent = 0;

		@Override
		public ICloseable indent() {
			final int post = ++indent;
			return () -> {
				if (indent != post) throw new IllegalStateException();
				indent--;
			};
		}

		@Override
		public void newline() {
			builder.append('\n');
			for (int i = 0; i < indent; i++) {
				builder.append('\t');
			}
		}

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

		protected final HTMLTag.Pretty pretty;

		protected final List<Property<IReflectiveHTMLElement, ?>> properties;

		protected final List<Property<IReflectiveHTMLElement, ?>> content;

		public ReflectiveExplicitHTMLElement(IReflectiveHTMLElement element) {
			this.element = element;

			{ // Compute the tag
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
							this.tag = annotation.generator().newInstance().apply(element);
						} catch (InstantiationException | IllegalAccessException exception) {
							throw new ReflectionException(exception);
						}
					} else this.tag = element.getClass().getSimpleName().toLowerCase();

					this.pretty = annotation.pretty();
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
				final ContentState[] state = new ContentState[] { ContentState.Empty };
				final int prepLength = builder.length();
				if (content != null) {
					final IConsumer2<Object, Type> accepter = (value, type) -> {
						if (state[0] == ContentState.Empty) {
							builder.append('>');
							state[0] = ContentState.Prepped;
						}

						if (pretty != HTMLTag.Pretty.Inline) context.newline();

						final int length = builder.length();
						context.toExplicit(value, type).render(context);
						if (length != builder.length()) state[0] = ContentState.Written;
						else builder.setLength(length - 1);
					};

					try (final ICloseable close = (pretty == HTMLTag.Pretty.NoIndent) ? () -> {} : context.indent()) {
						for (Property<IReflectiveHTMLElement, ?> property : content) {
							final Object value = property.getAccessor().apply(element);
							if (!property.getField().skipNull() || (value != null)) {
								if (value instanceof Collection) {
									final Collection<?> collection = ((Collection<?>) value);
									collection.forEach(child -> accepter.accept(child, null));
								} else accepter.accept(value, property.getType());
							}
						}
					}
				}

				switch (state[0]) {
					case Empty:
						// No content was even found, so just wrap up
						builder.append("/>");
						break;
					case Prepped:
						// We found content, but it didn't actually write anything to change the ">" at the end of the buffer to a "/>"
						if (prepLength == -1) throw new IllegalStateException();
						builder.setLength(prepLength);
						builder.append("/>");
						break;
					case Written:
						// Content was written so actually write the closing tag
						if (pretty != HTMLTag.Pretty.Inline) context.newline();
						builder.append("</").append(tag).append('>');
						break;
				}
			}
		}
	}

	public String render(Object element) {
		final StringBuilder retVal = new StringBuilder();
		final HTMLRenderContext context = new HTMLRenderContext(retVal);
		context.toExplicit(element, null).render(context);
		return retVal.toString();
	}

}
