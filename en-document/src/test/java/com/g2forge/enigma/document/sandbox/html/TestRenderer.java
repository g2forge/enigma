package com.g2forge.enigma.document.sandbox.html;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.document.sandbox.html.HTMLRenderer;
import com.g2forge.enigma.document.sandbox.html.elements.Image;
import com.g2forge.enigma.document.sandbox.html.elements.Span;

public class TestRenderer {
	protected final HTMLRenderer renderer = new HTMLRenderer();

	@Test
	public void contents() {
		Assert.assertEquals("<span>foo<span>bar</span></span>", renderer.render(new Span(null, "foo", new Span(null, "bar"))));
	}

	@Test
	public void image() {
		Assert.assertEquals("<img src=\"http://www.example.com/picture.jpg\"/>", renderer.render(Image.builder().src("http://www.example.com/picture.jpg").build()));
	}

	@Test
	public void span() {
		Assert.assertEquals("<span/>", renderer.render(Span.builder().build()));
		Assert.assertEquals("<span>foo</span>", renderer.render(new Span(null, "foo")));
		Assert.assertEquals("<span id=\"juan\"/>", renderer.render(Span.builder().id("juan").build()));
		Assert.assertEquals("<span id=\"juan\">foo</span>", renderer.render(Span.builder().id("juan").content("foo").build()));
	}
}
