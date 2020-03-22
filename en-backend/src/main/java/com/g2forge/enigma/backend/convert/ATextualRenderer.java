package com.g2forge.enigma.backend.convert;

import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.enigma.backend.text.convert.TextRenderer;
import com.g2forge.enigma.backend.text.model.expression.ITextExpression;

public abstract class ATextualRenderer<R, C extends IRenderContext<? super C> & IBuilder<? extends ITextExpression>> extends ARenderer<R, C> {
	protected static final TextRenderer textRenderer = new TextRenderer();

	public String render(R renderable) {
		final C context = createContext();
		context.render(renderable, null);
		final ITextExpression text = context.build();
		return textRenderer.render(text);
	}
}
