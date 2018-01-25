package com.g2forge.enigma.document.sandbox.css;

@FunctionalInterface
public interface IExplicitCSSRenderable extends ICSSRenderable {
	public void render(ICSSRenderContext context);
}
