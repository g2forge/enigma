package com.g2forge.enigma.document.convert.md.linebreak;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.enigma.document.convert.md.IMDRenderContext;
import com.g2forge.enigma.document.model.Block;
import com.g2forge.enigma.document.model.IDocListItem;

public enum LineBreakStrategy implements ILineBreakStrategy {
	None,
	Item {
		@Override
		public void beforeItem(IMDRenderContext context, boolean first, IDocListItem item) {
			if (!first) context.newline().newline();
		}
	},
	Period {
		@Override
		public void text(IMDRenderContext context, String text) {
			final Pattern pattern = Pattern.compile("\\.\\s+");
			final Matcher matcher = pattern.matcher(text);
			int index = 0;
			while (matcher.find()) {
				context.append(text.substring(index, matcher.start() + 1)).newline();
				index = matcher.end();
			}
			context.append(text.substring(index, text.length()));
		}
	};

	public static ILineBreakStrategy fromBlockType(IMDRenderContext context, Block.Type type) {
		final LineBreakStrategy base;
		switch (type) {
			case Document:
			case Block:
				base = Item;
				break;
			case Paragraph:
			case ListItem:
				base = Period;
				break;
			default:
				throw new EnumException(Block.Type.class, type);
		}
		if (context.getIndentLevel() > 0) return new ListLineBreakStrategy(base);
		return base;
	}

	public void beforeItem(IMDRenderContext context, boolean first, IDocListItem item) {}

	public void text(IMDRenderContext context, String text) {
		context.append(text);
	}
}