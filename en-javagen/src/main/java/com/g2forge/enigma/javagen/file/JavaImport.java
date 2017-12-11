package com.g2forge.enigma.javagen.file;

import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JavaImport {
	protected static final String TEMPLATE = "import <type>;";

	protected final JavaType type;
}
