package com.g2forge.enigma.javagen.codegen;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JavaType {
	protected String type;

	public JavaType(Class<?> type) {
		this(type.getName());
	}

	public String getReference() {
		final String type = getType();
		if (IJavaEnvironment.getEnvironment().isImported(this)) {
			final int lastDot = type.lastIndexOf('.');
			if (lastDot > -1) return type.substring(lastDot + 1);
		}
		return type;
	}
}
