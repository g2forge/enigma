package com.g2forge.enigma.javagen.core;

public enum JavaModifier {
	Abstract,
	Final,
	Static,
	Transient,
	Volative;

	protected static final String TEMPLATE = "<text>";

	protected String getText() {
		return name().toLowerCase();
	}
}
