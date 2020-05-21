package com.g2forge.enigma.web.html.convert;

@FunctionalInterface
public interface IExplicitHTMLRenderable extends IHTMLElement {
	public void render(IHTMLRenderContext context);
}
