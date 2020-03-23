package com.g2forge.enigma.backend.convert;

import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.enigma.backend.text.convert.TextRenderer;
import com.g2forge.enigma.backend.text.model.expression.ITextExpression;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class ATextualRenderer<R, C extends IRenderContext<? super C> & IBuilder<? extends ITextExpression>> extends ARenderer<R, C> {
	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final TextRenderer textRenderer = new TextRenderer();

	@Override
	protected String build(final C context) {
		final ITextExpression text = context.build();
		return getTextRenderer().render(text);
	}
}
