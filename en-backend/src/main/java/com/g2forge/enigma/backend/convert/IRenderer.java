package com.g2forge.enigma.backend.convert;

import com.g2forge.alexandria.java.function.IFunction1;

@FunctionalInterface
public interface IRenderer<R> {
	public default <_R> IRenderer<_R> adapt(IFunction1<? super _R, ? extends R> function) {
		return r -> render(function.apply(r));
	}

	public String render(R renderable);
}
