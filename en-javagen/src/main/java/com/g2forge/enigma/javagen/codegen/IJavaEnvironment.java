package com.g2forge.enigma.javagen.codegen;

import java.util.Stack;

import com.g2forge.alexandria.java.close.ICloseable;

public interface IJavaEnvironment {
	static final ThreadLocal<Stack<IJavaEnvironment>> locals = ThreadLocal.withInitial(Stack::new);

	public static IJavaEnvironment getEnvironment() {
		return locals.get().peek();
	}

	public static ICloseable open(final IJavaEnvironment environment) {
		locals.get().push(environment);
		return new ICloseable() {
			@Override
			public void close() {
				if (locals.get().pop() != environment) throw new RuntimeException("Java environment stack became corrupted at some point!");
			}
		};
	}

	public boolean isImported(JavaType type);
}
