package com.g2forge.enigma.document.sandbox.wikitext;

import org.eclipse.mylyn.wikitext.parser.Attributes;
import org.eclipse.mylyn.wikitext.parser.DocumentBuilder;

import com.g2forge.enigma.document.sandbox.wikitext.doc.IWikitextBuilder;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WikitextDocumentBuilder extends DocumentBuilder {
	protected IWikitextBuilder<?> current;

	@Override
	public void acronym(String text, String definition) {
		setCurrent(current.acronym(text, definition));
	}

	@Override
	public void beginBlock(BlockType type, Attributes attributes) {
		setCurrent(current.beginBlock(type, attributes));
	}

	@Override
	public void beginDocument() {
		setCurrent(current.beginDocument());
	}

	@Override
	public void beginHeading(int level, Attributes attributes) {
		setCurrent(current.beginHeading(level, attributes));
	}

	@Override
	public void beginSpan(SpanType type, Attributes attributes) {
		setCurrent(current.beginSpan(type, attributes));
	}

	@Override
	public void characters(String text) {
		setCurrent(current.characters(text));
	}

	@Override
	public void charactersUnescaped(String literal) {
		setCurrent(current.charactersUnescaped(literal));
	}

	@Override
	public void endBlock() {
		setCurrent(current.endBlock());
	}

	@Override
	public void endDocument() {
		setCurrent(current.endDocument());
	}

	@Override
	public void endHeading() {
		setCurrent(current.endHeading());
	}

	@Override
	public void endSpan() {
		setCurrent(current.endSpan());
	}

	@Override
	public void entityReference(String entity) {
		setCurrent(current.entityReference(entity));
	}

	@Override
	public void image(Attributes attributes, String url) {
		setCurrent(current.image(attributes, url));
	}

	@Override
	public void imageLink(Attributes linkAttributes, Attributes imageAttributes, String href, String imageUrl) {
		setCurrent(current.imageLink(linkAttributes, imageAttributes, href, imageUrl));
	}

	@Override
	public void lineBreak() {
		setCurrent(current.lineBreak());
	}

	@Override
	public void link(Attributes attributes, String hrefOrHashName, String text) {
		setCurrent(current.link(attributes, hrefOrHashName, text));
	}

	protected void setCurrent(IWikitextBuilder<?> current) {
		this.current = current;
	}
}
