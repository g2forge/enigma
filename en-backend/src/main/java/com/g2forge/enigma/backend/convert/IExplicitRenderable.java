package com.g2forge.enigma.backend.convert;

@FunctionalInterface
public interface IExplicitRenderable<C> extends IRenderable {
	public void render(C context);
}
