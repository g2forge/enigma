package com.g2forge.enigma.presentation.content;

import com.g2forge.enigma.presentation.PresentationBuilder.ContentContext;

@FunctionalInterface
public interface IContentManual extends IContent {
	public void apply(ContentContext context);
}
