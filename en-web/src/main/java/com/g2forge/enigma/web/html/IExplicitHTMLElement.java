package com.g2forge.enigma.web.html;

@FunctionalInterface
public interface IExplicitHTMLElement extends IHTMLElement {
	public void render(IHTMLRenderContext context);
}
