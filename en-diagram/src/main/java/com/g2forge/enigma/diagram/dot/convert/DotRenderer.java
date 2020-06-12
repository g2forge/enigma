package com.g2forge.enigma.diagram.dot.convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.java.text.quote.QuoteControl;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.alexandria.reflection.object.HReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.convert.text.ControlQuoteTextModifier;
import com.g2forge.enigma.backend.convert.textual.ATextualRenderer;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified.TextNestedModifiedBuilder;
import com.g2forge.enigma.diagram.dot.model.DotEdge;
import com.g2forge.enigma.diagram.dot.model.DotEdgeDirection;
import com.g2forge.enigma.diagram.dot.model.DotGraph;
import com.g2forge.enigma.diagram.dot.model.DotNode;
import com.g2forge.enigma.diagram.dot.model.DotNodeShape;
import com.g2forge.enigma.diagram.dot.model.IDotAttribute;
import com.g2forge.enigma.diagram.dot.model.IDotAttributed;
import com.g2forge.enigma.diagram.dot.model.IDotStatement;
import com.g2forge.enigma.diagram.dot.model.StringDotAttribute;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DotRenderer extends ATextualRenderer<Object, IDotRenderContext> {
	protected class DotRenderContext extends ARenderContext implements IDotRenderContext {
		protected Boolean directed = null;

		public DotRenderContext(TextNestedModifiedBuilder builder) {
			super(builder);
		}

		@Override
		protected IDotRenderContext getThis() {
			return this;
		}

		@Override
		public boolean isDirected() {
			if (directed == null) throw new IllegalStateException();
			return directed;
		}

		@Override
		public ICloseable quote(QuoteControl control) {
			return getBuilder().open(new ControlQuoteTextModifier(DotStringValueQuoteType.create(), control));
		}

		@Override
		public ICloseable setDirected(boolean directed) {
			if (this.directed != null) throw new IllegalStateException();
			this.directed = directed;
			return () -> this.directed = null;
		}
	}

	protected static class DotRendering extends ARenderer.ARendering<Object, IDotRenderContext, IExplicitRenderable<? super IDotRenderContext>> {
		protected final Map<Class<? extends IDotAttributed>, List<? extends IFunction1<? extends IDotAttributed, ? extends IDotAttribute>>> reflections = new HashMap<>();

		protected void attributes(IDotAttributed e, IDotRenderContext c) {
			final List<IDotAttribute> attributes = computeAttributes(e);
			if ((attributes != null) && !attributes.isEmpty()) {
				c.append(" [");
				boolean first = true;
				for (IDotAttribute attribute : attributes) {
					if (first) first = false;
					else c.append(", ");
					c.render(attribute, IDotAttribute.class);
				}
				c.append(']');
			}
		}

		protected <T extends IDotAttributed> List<? extends IFunction1<T, ? extends IDotAttribute>> computeAccessors(final Class<T> klass) {
			final Stream<? extends IJavaFieldReflection<T, ?>> fields = HReflection.toReflection(klass).getFields(JavaScope.Inherited, null).filter(field -> field.getAnnotations().getAnnotation(DotAttribute.class) != null);
			return fields.map(field -> {
				final DotAttribute annotation = field.getAnnotations().getAnnotation(DotAttribute.class);
				final String name = annotation.value().trim().length() == 0 ? field.getType().getName() : annotation.value();
				return IFunction1.create((T attributed) -> {
					final Object value = field.getAccessor(attributed).get0();
					if (annotation.skipNull() && (value == null)) return null;
					if (value instanceof String) return new StringDotAttribute(name, (String) value);
					return new ObjectDotAttribute(name, value);
				});
			}).collect(Collectors.toList());
		}

		protected List<IDotAttribute> computeAttributes(IDotAttributed e) {
			final List<? extends IFunction1<? extends IDotAttributed, ? extends IDotAttribute>> accessors = reflections.computeIfAbsent(e.getClass(), this::computeAccessors);
			@SuppressWarnings("unchecked")
			final List<? extends IFunction1<IDotAttributed, ? extends IDotAttribute>> cast = (List<? extends IFunction1<IDotAttributed, ? extends IDotAttribute>>) accessors;
			return computeAttributes(e, cast);
		}

		protected <T extends IDotAttributed> List<IDotAttribute> computeAttributes(T attributed, final List<? extends IFunction1<T, ? extends IDotAttribute>> accessors) {
			final List<? extends IDotAttribute> reflected = accessors.stream().map(f -> f.apply(attributed)).filter(Objects::nonNull).collect(Collectors.toList());
			final List<IDotAttribute> explicit = attributed.getAttributes();
			return HCollection.concatenate(reflected, explicit);
		}

		@Override
		protected void extend(TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super IDotRenderContext>> builder) {
			builder.add(IExplicitDotRenderable.class, e -> c -> e.render(c));
			ITextAppender.addToBuilder(builder, new ITextAppender.IExplicitFactory<IDotRenderContext, IExplicitRenderable<? super IDotRenderContext>>() {
				@Override
				public <T> IFunction1<? super T, ? extends IExplicitRenderable<? super IDotRenderContext>> create(IConsumer2<? super IDotRenderContext, ? super T> consumer) {
					return e -> c -> consumer.accept(c, e);
				}
			});

			builder.add(DotGraph.class, e -> c -> {
				if (e.isDirected()) c.append("di");
				try (final ICloseable directed = c.setDirected(e.isDirected())) {
					c.append("graph ").append(e.getName()).append(" {");
					final List<IDotStatement> statements = e.getStatements();
					if ((statements != null) && !statements.isEmpty()) {
						c.newline();
						try (ICloseable indent = c.indent()) {
							for (IDotStatement statement : statements) {
								c.render(statement, IDotStatement.class).append(';').newline();
							}
						}
					}
					c.append('}');
				}
			});

			builder.add(DotNode.class, e -> c -> {
				c.append(e.getName());
				attributes(e, c);
			});
			builder.add(DotEdge.class, e -> c -> {
				boolean first = true;
				for (String nodeName : e.getNodes()) {
					if (first) first = false;
					else c.append(c.isDirected() ? " -> " : " -- ");
					c.append(nodeName);
				}
				attributes(e, c);
			});

			builder.add(StringDotAttribute.class, e -> c -> {
				c.append(e.getName()).append('=');
				if (e.getValue().length() < 1) c.append("\"\"");
				else try (ICloseable quote = c.quote(QuoteControl.Always)) {
					c.render(e.getValue(), String.class);
				}
			});
			builder.add(ObjectDotAttribute.class, e -> c -> {
				c.append(e.getName()).append('=');
				try (ICloseable quote = c.quote((e.getValue() instanceof Enum) ? QuoteControl.IfNeeded : QuoteControl.Always)) {
					c.render(e.getValue(), Object.class);
				}
			});

			builder.add(DotNodeShape.class, e -> c -> c.append(e.name().toLowerCase()));
			builder.add(DotEdgeDirection.class, e -> c -> c.append(e.name().toLowerCase()));
		}
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	protected static class ObjectDotAttribute implements IDotAttribute {
		protected final String name;

		protected final Object value;
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final IRendering<Object, IDotRenderContext, IExplicitRenderable<? super IDotRenderContext>> renderingStatic = new DotRendering();

	@Override
	protected IDotRenderContext createContext(TextNestedModifiedBuilder builder) {
		return new DotRenderContext(builder);
	}

	@Override
	protected IRendering<? super Object, ? extends IDotRenderContext, ? extends IExplicitRenderable<? super IDotRenderContext>> getRendering() {
		return getRenderingStatic();
	}
}
