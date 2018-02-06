package com.g2forge.enigma.presentation.content;

import com.g2forge.enigma.presentation.IContentContext;

@FunctionalInterface
public interface IContentManual extends IContent {
	public void apply(IContentContext context);
}
