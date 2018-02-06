package com.g2forge.enigma.document.convert.md;

import com.g2forge.enigma.document.convert.IDocElement;

@FunctionalInterface
public interface IExplicitMDElement extends IDocElement {
	public void render(IMDRenderContext context);
}
