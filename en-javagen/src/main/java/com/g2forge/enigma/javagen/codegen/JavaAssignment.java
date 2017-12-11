package com.g2forge.enigma.javagen.codegen;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JavaAssignment implements IJavaStatement {
	protected IJavaExpression left;

	protected IJavaExpression right;
}
