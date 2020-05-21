package com.g2forge.enigma.backend.text.model.expression;

import com.g2forge.alexandria.java.core.marker.ISingleton;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TextNothing implements ITextExpression, ISingleton {
	protected static final TextNothing INSTANCE = new TextNothing();

	public static TextNothing create() {
		return INSTANCE;
	}

	protected TextNothing() {}
}
