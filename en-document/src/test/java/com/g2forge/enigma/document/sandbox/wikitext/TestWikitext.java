package com.g2forge.enigma.document.sandbox.wikitext;

import org.eclipse.mylyn.wikitext.markdown.MarkdownLanguage;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.document.sandbox.wikitext.doc.DocumentBuilder;

public class TestWikitext {
	@Test
	public void parse() {
		final DocumentBuilder builder = new DocumentBuilder();
		final MarkupParser parser = new MarkupParser(new MarkdownLanguage(), new WikitextDocumentBuilder(builder));
		parser.parse("Text");
		Assert.assertEquals(Block.builder().content(new Text("Text")).build(), builder.build());
	}
}
