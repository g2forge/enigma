package com.g2forge.enigma.diagram.dot.convert;

import java.util.List;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.quote.QuoteControl;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.convert.textual.ATextualRenderer;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified.TextNestedModifiedBuilder;
import com.g2forge.enigma.diagram.dot.model.StringDotAttribute;
import com.g2forge.enigma.diagram.dot.model.DotEdge;
import com.g2forge.enigma.diagram.dot.model.DotGraph;
import com.g2forge.enigma.diagram.dot.model.DotNode;
import com.g2forge.enigma.diagram.dot.model.IDotAttribute;
import com.g2forge.enigma.diagram.dot.model.IDotAttributed;
import com.g2forge.enigma.diagram.dot.model.IDotStatement;

import lombok.AccessLevel;
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
		public ICloseable setDirected(boolean directed) {
			if (this.directed != null) throw new IllegalStateException();
			this.directed = directed;
			return () -> this.directed = null;
		}
	}

	protected static class DotRendering extends ARenderer.ARendering<Object, IDotRenderContext, IExplicitRenderable<? super IDotRenderContext>> {
		protected void attributes(IDotAttributed e, IDotRenderContext c) {
			final List<IDotAttribute> attributes = e.getAttributes();
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
				final String quoted = DotStringValueQuoteType.create().quote(QuoteControl.IfNeeded, e.getValue());
				c.append(e.getName()).append('=').append(quoted);
			});
		}
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
