package com.g2forge.enigma.javagen.codegen;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JavaReference implements IJavaExpression {
	protected String name;
}
