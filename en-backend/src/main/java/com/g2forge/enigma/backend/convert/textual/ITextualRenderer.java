package com.g2forge.enigma.backend.convert.textual;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.enigma.backend.convert.IRenderer;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified.TextNestedModifiedBuilder;

public interface ITextualRenderer<R> extends IRenderer<R> {
	public default <_R> ITextualRenderer<_R> adapt(IFunction1<? super _R, ? extends R> function) {
		return new ITextualRenderer<_R>() {
			@Override
			public String render(_R renderable) {
				return ITextualRenderer.this.render(function.apply(renderable));
			}

			@Override
			public void render(TextNestedModifiedBuilder builder, _R renderable) {
				ITextualRenderer.this.render(builder, function.apply(renderable));
			}
		};
	}

	public void render(TextNestedModified.TextNestedModifiedBuilder builder, R renderable);
}
