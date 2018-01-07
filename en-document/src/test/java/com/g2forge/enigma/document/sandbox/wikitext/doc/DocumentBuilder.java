package com.g2forge.enigma.document.sandbox.wikitext.doc;

public class DocumentBuilder extends BlockBuilder {
	public DocumentBuilder() {
		super(null);
	}

	public IWikitextBuilder<?> beginDocument() {
		return this;
	}

	public IWikitextBuilder<?> endDocument() {
		return null;
	}
}
