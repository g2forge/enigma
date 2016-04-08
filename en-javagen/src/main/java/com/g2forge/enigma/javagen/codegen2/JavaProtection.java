package com.g2forge.enigma.javagen.codegen2;

public enum JavaProtection {
	Private,
	Unspecified {
		@Override
		protected String getKeyword() {
			return "";
		}
	},
	Protected,
	Public;

	protected static final String TEMPLATE = "<keyword><if(!empty)> <endif>";

	protected String getKeyword() {
		return name().toLowerCase();
	}

	protected boolean isEmpty() {
		return getKeyword().length() == 0;
	}
}
