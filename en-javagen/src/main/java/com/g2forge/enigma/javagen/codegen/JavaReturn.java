package com.g2forge.enigma.javagen.codegen;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JavaReturn implements IJavaStatement {
	protected IJavaExpression expression;
}
