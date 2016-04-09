package com.g2forge.enigma.javagen.codegen2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaAnnotation {
	protected static final String TEMPLATE = "@<type>";

	protected JavaType type;
}
