package com.g2forge.enigma.document.convert.md.linebreak;

import com.g2forge.enigma.document.convert.md.IMDRenderContext;
import com.g2forge.enigma.document.model.DocList;
import com.g2forge.enigma.document.model.IDocListItem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListLineBreakStrategy implements ILineBreakStrategy {
	protected final LineBreakStrategy base;

	@Override
	public void beforeItem(IMDRenderContext context, boolean first, IDocListItem item) {
		if (!first) {
			if (!(item instanceof DocList)) context.append("\\");
			context.newline();
		}
	}

	@Override
	public void text(IMDRenderContext context, String text) {
		context.append(text);
	}
}
