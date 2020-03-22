package com.g2forge.enigma.backend.convert.common;

import java.lang.reflect.Type;

@FunctionalInterface
public interface IRenderContext<T> {
	public T render(Object object, Type type);
}
