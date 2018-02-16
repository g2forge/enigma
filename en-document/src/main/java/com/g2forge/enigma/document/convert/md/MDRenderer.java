package com.g2forge.enigma.document.convert.md;

import java.lang.reflect.Type;
import java.util.Stack;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.enums.EnumException;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch1;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.Definition;
import com.g2forge.enigma.document.DocList;
import com.g2forge.enigma.document.Emphasis;
import com.g2forge.enigma.document.IBlock;
import com.g2forge.enigma.document.IDocListItem;
import com.g2forge.enigma.document.ISpan;
import com.g2forge.enigma.document.Link;
import com.g2forge.enigma.document.Section;
import com.g2forge.enigma.document.Span;
import com.g2forge.enigma.document.Text;

import lombok.Data;

public class MDRenderer {
	@Data
	protected static class MDRenderContext implements IMDRenderContext {
		protected static final IFunction1<Object, IExplicitMDElement> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitMDElement>().with(builder -> {
			builder.add(IExplicitMDElement.class, IFunction1.identity());
			builder.add(Text.class, md -> c -> c.getBuilder().append(md.getText()));
			builder.add(Span.class, md -> c -> {
				for (IBlock content : md.getContents()) {
					c.toExplicit(content, IBlock.class).render(c);
				}
			});
			builder.add(Link.class, md -> c -> {
				final String target = md.getTarget();
				final StringBuilder b = c.getBuilder();
				if (target != null) b.append('[');
				c.toExplicit(md.getBody(), ISpan.class).render(c);
				if (target != null) b.append("](").append(target).append(')');
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
				final StringBuilder b = c.getBuilder();
				b.append(delimiter);
				c.toExplicit(md.getSpan(), ISpan.class).render(c);
				b.append(delimiter);
			});

			builder.add(Block.class, md -> c -> {
				boolean first = true;
				final String newline = c.getNewline();
				final StringBuilder b = c.getBuilder();
				for (IBlock content : md.getContents()) {
					if (first) first = false;
					else b.append(newline).append(newline);
					c.toExplicit(content, IBlock.class).render(c);
				}
			});
			builder.add(DocList.class, md -> c -> {
				final StringBuilder b = c.getBuilder();
				final String newline = c.getNewline();
				final java.util.List<IDocListItem> items = md.getItems();
				for (int i = 0; i < items.size(); i++) {
					switch (md.getMarker()) {
						case Ordered:
							b.append("* ");
							break;
						case Numbered:
							b.append(String.format("%1$d. ", i + 1));
							break;
					}
					c.toExplicit(items.get(i), IBlock.class).render(c);
					if (i < (items.size() - 1)) b.append(newline);
				}
			});
			builder.add(Definition.class, md -> c -> {
				c.toExplicit(md.getTerm(), ISpan.class).render(c);
				c.getBuilder().append(": ");
				c.toExplicit(md.getBody(), ISpan.class).render(c);
			});
			builder.add(Section.class, md -> c -> {
				final String newline = c.getNewline();
				final StringBuilder b = c.getBuilder();
				for (int i = 0; i < c.getSectionLevel(); i++) {
					b.append('#');
				}
				b.append(" ");
				c.toExplicit(md.getTitle(), ISpan.class).render(c);
				b.append(newline).append(newline);
				try (final ICloseable section = c.openSection()) {
					c.toExplicit(md.getBody(), IBlock.class).render(c);
				}
			});
		}).build();

		protected final StringBuilder builder;

		protected final String newline;

		protected final Stack<ICloseable> stack = new Stack<>();

		@Override
		public int getSectionLevel() {
			return stack.size() + 1;
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

		@Override
		public IExplicitMDElement toExplicit(final Object element, Type type) {
			return toExplicit.apply(element);
		}
	}

	public String render(Object element) {
		final StringBuilder retVal = new StringBuilder();
		final MDRenderContext context = new MDRenderContext(retVal, "\n");
		context.toExplicit(element, null).render(context);
		return retVal.toString();
	}
}
