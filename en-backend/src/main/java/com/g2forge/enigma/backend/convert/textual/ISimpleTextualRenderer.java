package com.g2forge.enigma.backend.convert.textual;

import com.g2forge.enigma.backend.text.convert.TextRenderer;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified.TextNestedModifiedBuilder;

@FunctionalInterface
public interface ISimpleTextualRenderer<R> extends ITextualRenderer<R> {
	@Override
	public default String render(R renderable) {
		final TextNestedModifiedBuilder builder = TextNestedModified.builder();
		render(builder, renderable);
		return new TextRenderer().render(builder.build());
	}
}
