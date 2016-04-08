package com.g2forge.enigma.javagen.codegen2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaMethod implements IJavaMember {
	protected static final String TEMPLATE = "<protection;format=\"java\"><type;format=\"java\"> <name>(){}";

	protected JavaProtection protection;

	protected JavaType type;

	protected String name;
}
