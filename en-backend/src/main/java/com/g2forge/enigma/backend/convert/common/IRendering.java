package com.g2forge.enigma.backend.convert.common;

import java.lang.reflect.Type;

@FunctionalInterface
public interface IRendering<C, E extends IExplicitRenderable<C>> {
	public E toExplicit(Object object, Type type);
}
