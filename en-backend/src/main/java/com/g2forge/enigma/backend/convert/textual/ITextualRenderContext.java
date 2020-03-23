package com.g2forge.enigma.backend.convert.textual;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.enigma.backend.ITextBuilder;
import com.g2forge.enigma.backend.convert.IRenderContext;
import com.g2forge.enigma.backend.text.model.expression.ITextExpression;

public interface ITextualRenderContext<R, C extends ITextualRenderContext<R, ? super C>> extends IRenderContext<R, C>, ITextBuilder<C>, IBuilder<ITextExpression> {
	public ICloseable indent();
}
