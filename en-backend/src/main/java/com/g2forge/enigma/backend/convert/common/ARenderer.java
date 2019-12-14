package com.g2forge.enigma.backend.convert.common;

import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.enigma.backend.convert.TextRenderer;
import com.g2forge.enigma.backend.model.expression.ITextExpression;

public abstract class ARenderer<R, C extends IRenderContext<? super C> & IBuilder<? extends ITextExpression>> implements IRenderer<R> {
	protected static final TextRenderer textRenderer = new TextRenderer();

	protected abstract C createContext();

	public String render(R renderable) {
		final C context = createContext();
		context.render(renderable, null);
		final ITextExpression text = context.build();
		return textRenderer.render(text);
	}
}
