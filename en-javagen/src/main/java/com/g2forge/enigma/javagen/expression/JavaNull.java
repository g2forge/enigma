package com.g2forge.enigma.javagen.expression;

public class JavaNull implements IJavaExpression {
	protected static final String TEMPLATE = "null";

	public static final JavaNull singleton = new JavaNull();
}
