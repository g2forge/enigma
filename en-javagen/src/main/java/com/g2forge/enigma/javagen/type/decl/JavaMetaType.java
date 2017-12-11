package com.g2forge.enigma.javagen.type.decl;

public enum JavaMetaType {
	Class,
	Interface,
	Enum;

	protected static final String TEMPLATE = "<keyword>";

	protected String getKeyword() {
		return name().toLowerCase();
	}
}
