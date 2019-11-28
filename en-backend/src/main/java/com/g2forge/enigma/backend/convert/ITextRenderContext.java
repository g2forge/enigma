package com.g2forge.enigma.backend.convert;

import com.g2forge.enigma.backend.convert.common.IRenderContext;

public interface ITextRenderContext extends IRenderContext<ITextRenderContext> {
	public StringBuilder getBuilder();
}
