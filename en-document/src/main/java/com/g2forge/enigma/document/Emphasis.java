package com.g2forge.enigma.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Emphasis implements ISpan {
	public enum Type {
		Monospace,
		Code,
		Emphasis,
		Strong,
		Strikethrough;
	}

	protected final Type type;

	protected final ISpan span;
}
