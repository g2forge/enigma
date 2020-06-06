package com.g2forge.enigma.diagram.dot.convert;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.backend.convert.textual.ITextualRenderContext;

public interface IDotRenderContext extends ITextualRenderContext<Object, IDotRenderContext> {
	public ICloseable setDirected(boolean directed);

	public boolean isDirected();
}
