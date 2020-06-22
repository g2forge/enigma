package com.g2forge.enigma.document.convert.md;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.enigma.document.model.Block;

public enum LineBreakStrategy {
	None,
	Item {
		@Override
		public void beforeItem(IMDRenderContext context, boolean first) {
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

	public static LineBreakStrategy fromBlockType(Block.Type type) {
		switch (type) {
			case Document:
			case Block:
				return Item;
			case Paragraph:
			case ListItem:
				return Period;
			default:
				throw new EnumException(Block.Type.class, type);
		}
	}

	public void beforeItem(IMDRenderContext context, boolean first) {}

	public void text(IMDRenderContext context, String text) {
		context.append(text);
	}
}