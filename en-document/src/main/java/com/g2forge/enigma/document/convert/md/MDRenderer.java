package com.g2forge.enigma.document.convert.md;

import java.util.Stack;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.convert.textual.ATextualRenderer;
import com.g2forge.enigma.document.model.Block;
import com.g2forge.enigma.document.model.Definition;
import com.g2forge.enigma.document.model.DocList;
import com.g2forge.enigma.document.model.Emphasis;
import com.g2forge.enigma.document.model.IBlock;
import com.g2forge.enigma.document.model.IDocListItem;
import com.g2forge.enigma.document.model.ISpan;
import com.g2forge.enigma.document.model.Image;
import com.g2forge.enigma.document.model.Link;
import com.g2forge.enigma.document.model.Section;
import com.g2forge.enigma.document.model.Span;
import com.g2forge.enigma.document.model.Text;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MDRenderer extends ATextualRenderer<Object, IMDRenderContext> {
	protected class MDRenderContext extends ARenderContext implements IMDRenderContext {
		protected final Stack<LineBreakStrategy> lineBreakStrategies = new Stack<>();

		protected final Stack<ICloseable> stack = new Stack<>();

		@Override
		public LineBreakStrategy getLineBreakStrategy() {
			if (lineBreakStrategies.isEmpty()) return LineBreakStrategy.None;
			return lineBreakStrategies.peek();
		}

		@Override
		public int getSectionLevel() {
			return stack.size() + 1;
		}

		@Override
		protected IMDRenderContext getThis() {
			return this;
		}

		@Override
		public ICloseable openLineBreakStrategy(LineBreakStrategy strategy) {
			lineBreakStrategies.push(strategy);
			final int size = lineBreakStrategies.size();
			return () -> {
				if (lineBreakStrategies.size() != size) throw new IllegalStateException();
				lineBreakStrategies.pop();
			};
		}

		@Override
		public ICloseable openSection() {
			final ICloseable retVal = new ICloseable() {
				@Override
				public void close() {
					if (stack.peek() != this) throw new IllegalArgumentException();
					stack.pop();
				}
			};
			stack.push(retVal);
			return retVal;
		}
	}

	protected static class MDRendering extends ARenderer.ARendering<Object, IMDRenderContext, IExplicitRenderable<? super IMDRenderContext>> {
		@Override
		protected void extend(TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super IMDRenderContext>> builder) {
			builder.add(IExplicitMDRenderable.class, e -> c -> e.render(c));
			ITextAppender.addToBuilder(builder, new ITextAppender.IExplicitFactory<IMDRenderContext, IExplicitRenderable<? super IMDRenderContext>>() {
				@Override
				public <T> IFunction1<? super T, ? extends IExplicitRenderable<? super IMDRenderContext>> create(IConsumer2<? super IMDRenderContext, ? super T> consumer) {
					return e -> c -> consumer.accept(c, e);
				}
			});

			builder.add(Text.class, md -> c -> c.getLineBreakStrategy().text(c, md.getText()));
			builder.add(Span.class, md -> c -> {
				for (IBlock content : md.getContents()) {
					c.render(content, IBlock.class);
				}
			});
			builder.add(Link.class, md -> c -> {
				final String target = md.getTarget();
				if (target != null) c.append('[');
				c.render(md.getBody(), ISpan.class);
				if (target != null) c.append("](").append(target).append(')');
			});
			builder.add(Emphasis.class, md -> c -> {
				final String delimiter;
				switch (md.getType()) {
					case Code:
						delimiter = "`";
						break;
					case Emphasis:
						delimiter = "*";
						break;
					case Strong:
						delimiter = "**";
						break;
					case Strikethrough:
						delimiter = "~~";
						break;
					default:
						throw new EnumException(Emphasis.Type.class, md.getType());
				}
				c.append(delimiter).render(md.getSpan(), ISpan.class).append(delimiter);
			});

			builder.add(Block.class, md -> c -> {
				try (final ICloseable strategy = c.openLineBreakStrategy(LineBreakStrategy.fromBlockType(md.getType()))) {
					boolean first = true;
					for (IBlock content : md.getContents()) {
						if (first) first = false;
						else c.getLineBreakStrategy().beforeItem(c, first);
						c.render(content, IBlock.class);
					}
				}
			});
			builder.add(DocList.class, md -> c -> {
				final java.util.List<IDocListItem> items = md.getItems();
				for (int i = 0; i < items.size(); i++) {
					switch (md.getMarker()) {
						case Ordered:
							c.append("* ");
							break;
						case Numbered:
							c.append(String.format("%1$d. ", i + 1));
							break;
					}
					c.render(items.get(i), IBlock.class);
					if (i < (items.size() - 1)) c.newline();
				}
			});
			builder.add(Definition.class, md -> c -> c.render(md.getTerm(), ISpan.class).append(": ").render(md.getBody(), ISpan.class));
			builder.add(Section.class, md -> c -> {
				for (int i = 0; i < c.getSectionLevel(); i++) {
					c.append('#');
				}
				c.append(" ").render(md.getTitle(), ISpan.class).newline().newline();
				try (final ICloseable section = c.openSection()) {
					c.render(md.getBody(), IBlock.class);
				}
			});
			builder.add(Image.class, md -> c -> c.append("![").append(md.getAlt()).append("](").append(md.getUrl()).append(')'));
		}
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final IRendering<Object, IMDRenderContext, IExplicitRenderable<? super IMDRenderContext>> renderingStatic = new MDRendering();

	@Override
	protected IMDRenderContext createContext() {
		return new MDRenderContext();
	}

	@Override
	protected IRendering<? super Object, ? extends IMDRenderContext, ? extends IExplicitRenderable<? super IMDRenderContext>> getRendering() {
		return getRenderingStatic();
	}
}
