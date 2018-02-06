package com.g2forge.enigma.web.css;

@FunctionalInterface
public interface IExplicitCSSRenderable extends ICSSRenderable {
	public void render(ICSSRenderContext context);
}
