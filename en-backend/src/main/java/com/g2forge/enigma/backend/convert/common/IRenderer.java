package com.g2forge.enigma.backend.convert.common;

public interface IRenderer<R extends IRenderable> {
	public String render(R renderable);
}
