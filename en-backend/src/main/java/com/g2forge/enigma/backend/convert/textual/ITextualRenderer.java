package com.g2forge.enigma.backend.convert.textual;

import com.g2forge.enigma.backend.convert.IRenderer;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;

public interface ITextualRenderer<R> extends IRenderer<R> {
	public void render(TextNestedModified.TextNestedModifiedBuilder builder, R renderable);
}
