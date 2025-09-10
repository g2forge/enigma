package com.g2forge.enigma.document.convert.md.linebreak;

import com.g2forge.enigma.document.convert.md.IMDRenderContext;
import com.g2forge.enigma.document.model.IDocListItem;

public interface ILineBreakStrategy {
	public void beforeItem(IMDRenderContext context, boolean first, IDocListItem item);

	public void text(IMDRenderContext context, String text);
}
