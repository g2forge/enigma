package com.g2forge.enigma.javagen.codegen;

import lombok.Getter;

public enum JavaProtection {
	Public,
	PackageProtected(null),
	Protected,
	Private;

	@Getter
	protected final String keyword;

	private JavaProtection() {
		this.keyword = name().toLowerCase();
	}

	private JavaProtection(String keyword) {
		this.keyword = keyword;
	}
}
