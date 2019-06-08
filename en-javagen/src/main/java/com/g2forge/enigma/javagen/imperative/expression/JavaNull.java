package com.g2forge.enigma.javagen.imperative.expression;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class JavaNull implements IJavaExpression, ISingleton {
	protected static final String TEMPLATE = "null";

	public static final JavaNull singleton = new JavaNull();
}
