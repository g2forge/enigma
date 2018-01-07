package com.g2forge.enigma.document.sandbox.wikitext.doc;

import org.eclipse.mylyn.wikitext.parser.Attributes;
import org.eclipse.mylyn.wikitext.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.parser.DocumentBuilder.SpanType;

public interface IWikitextBuilder<T> {
	public default IWikitextBuilder<?> acronym(String text, String definition) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> beginBlock(BlockType type, Attributes attributes) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> beginDocument() {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> beginHeading(int level, Attributes attributes) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> beginSpan(SpanType type, Attributes attributes) {
		throw new UnsupportedOperationException();
	}

	public T build();

	public default IWikitextBuilder<?> characters(String text) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> charactersUnescaped(String literal) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> endBlock() {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> endDocument() {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> endHeading() {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> endSpan() {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> entityReference(String entity) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> image(Attributes attributes, String url) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> imageLink(Attributes linkAttributes, Attributes imageAttributes, String href, String imageUrl) {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> lineBreak() {
		throw new UnsupportedOperationException();
	}

	public default IWikitextBuilder<?> link(Attributes attributes, String hrefOrHashName, String text) {
		throw new UnsupportedOperationException();
	}
}
