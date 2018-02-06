package com.g2forge.enigma.document.convert.md;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch1;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.IBlock;
import com.g2forge.enigma.document.List;
import com.g2forge.enigma.document.Text;

import lombok.Data;

public class MDRenderer {
	@Data
	protected static class MDRenderContext implements IMDRenderContext {
		protected static final IFunction1<Object, IExplicitMDElement> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitMDElement>().with(builder -> {
			builder.add(IExplicitMDElement.class, IFunction1.identity());
			builder.add(Text.class, md -> c -> c.getBuilder().append(md.getText()));
			builder.add(Block.class, md -> c -> {
				boolean first = true;
				final StringBuilder b = c.getBuilder();
				for (IBlock content : md.getContents()) {
					if (first) first = false;
					else {
						final String newline = c.getNewline();
						b.append(newline).append(newline);
					}
					c.toExplicit(content, IBlock.class).render(c);
				}
			});
			builder.add(List.class, md -> c -> {
				final StringBuilder b = c.getBuilder();
				final String newline = c.getNewline();
				final java.util.List<IBlock> items = md.getItems();
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
		}).build();

		protected final StringBuilder builder;

		protected final String newline;

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
