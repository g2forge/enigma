package com.g2forge.enigma.backend.model.expression;

import com.g2forge.alexandria.java.core.marker.ISingleton;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TextNewline implements ITextExpression, ISingleton {
	protected static final TextNewline INSTANCE = new TextNewline();

	public static TextNewline create() {
		return INSTANCE;
	}

	protected TextNewline() {}
}
