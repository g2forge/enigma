package com.g2forge.enigma.backend.convert;

import java.lang.reflect.Type;

@FunctionalInterface
public interface IRenderContext<C> {
	public C render(Object object, Type type);
}
