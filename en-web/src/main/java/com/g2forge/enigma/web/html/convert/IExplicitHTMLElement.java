package com.g2forge.enigma.web.html.convert;

@FunctionalInterface
public interface IExplicitHTMLElement extends IHTMLElement {
	public void render(IHTMLRenderContext context);
}
