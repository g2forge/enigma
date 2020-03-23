package com.g2forge.enigma.backend.convert;

import java.lang.reflect.Type;

@FunctionalInterface
public interface IRendering<R, C, E extends IExplicitRenderable<? super C>> {
	public E toExplicit(R object, Type type);
}
