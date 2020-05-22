package com.g2forge.enigma.document.convert.md;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.backend.convert.textual.ITextualRenderContext;

public interface IMDRenderContext extends ITextualRenderContext<Object, IMDRenderContext> {
	public LineBreakStrategy getLineBreakStrategy();

	public int getSectionLevel();

	public ICloseable openLineBreakStrategy(LineBreakStrategy strategy);

	public ICloseable openSection();
}
