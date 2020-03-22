package com.g2forge.enigma.backend.convert;

public abstract class ARenderer<R, C extends IRenderContext<? super C>> implements IRenderer<R> {
	protected abstract C createContext();
}
