package com.g2forge.enigma.presentation.content.document;

import com.g2forge.enigma.document.convert.IDocElement;

@FunctionalInterface
public interface IExplicitXSLFElement extends IDocElement {
	public void render(IXSLFRenderContext context);
}
