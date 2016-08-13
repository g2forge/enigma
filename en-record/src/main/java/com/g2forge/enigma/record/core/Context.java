package com.g2forge.enigma.record.core;

import lombok.Data;

@Data
public class Context {
	public enum Usage {
		Argument,
		Return,
		Parent,
		Field,
		Declaration;
	}

	protected final Usage usage;
}
