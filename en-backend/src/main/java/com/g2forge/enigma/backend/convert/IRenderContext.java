package com.g2forge.enigma.backend.convert;

import java.lang.reflect.Type;

@FunctionalInterface
public interface IRenderContext<R, C> {
	public C render(R object, Type type);
}
