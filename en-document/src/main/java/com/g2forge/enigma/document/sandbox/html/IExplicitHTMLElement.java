package com.g2forge.enigma.document.sandbox.html;

@FunctionalInterface
public interface IExplicitHTMLElement extends IHTMLElement {
	public void render(IHTMLRenderContext context);
}
