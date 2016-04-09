package com.g2forge.enigma.javagen.codegen2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaImport {
	protected static final String TEMPLATE = "import <type>;";
	
	protected JavaType type;
}
