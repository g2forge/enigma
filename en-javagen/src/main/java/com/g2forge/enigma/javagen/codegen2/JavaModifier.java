package com.g2forge.enigma.javagen.codegen2;

public enum JavaModifier {
	Final,
	Static,
	Transient,
	Volative;

	protected static final String TEMPLATE = "<text>";

	protected String getText() {
		return name().toLowerCase();
	}
}
