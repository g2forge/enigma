package com.g2forge.enigma.javagen.codegen2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaField {
	protected static final String TEMPLATE = "<type;format=\"java\"> <name>;";

	protected JavaType type;

	protected String name;
}
