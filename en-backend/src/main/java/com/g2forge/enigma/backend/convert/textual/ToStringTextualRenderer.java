package com.g2forge.enigma.backend.convert.textual;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified.TextNestedModifiedBuilder;

public class ToStringTextualRenderer<T> implements ITextualRenderer<T>, ISingleton {
	protected static final ToStringTextualRenderer<?> INSTANCE = new ToStringTextualRenderer<>();

	@SuppressWarnings("unchecked")
	public static <T> ToStringTextualRenderer<T> create() {
		return (ToStringTextualRenderer<T>) INSTANCE;
	}

	protected ToStringTextualRenderer() {}

	@Override
	public String render(T renderable) {
		return renderable.toString();
	}

	@Override
	public void render(TextNestedModifiedBuilder builder, T renderable) {
		builder.expression(renderable);
	}
}
