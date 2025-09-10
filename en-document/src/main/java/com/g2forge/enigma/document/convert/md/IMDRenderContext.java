package com.g2forge.enigma.document.convert.md;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.backend.convert.textual.ITextualRenderContext;
import com.g2forge.enigma.document.convert.md.linebreak.ILineBreakStrategy;

public interface IMDRenderContext extends ITextualRenderContext<Object, IMDRenderContext> {
	public ILineBreakStrategy getLineBreakStrategy();

	public int getSectionLevel();

	public int getIndentLevel();

	public ICloseable openLineBreakStrategy(ILineBreakStrategy strategy);

	public ICloseable openSection();
	
	public ICloseable openIndent();
}
