package com.g2forge.enigma.javagen.core;

import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JavaAnnotation {
	protected static final String TEMPLATE = "@<type>";

	protected final JavaType type;
}
