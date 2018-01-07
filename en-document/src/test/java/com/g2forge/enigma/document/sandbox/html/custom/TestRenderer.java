package com.g2forge.enigma.document.sandbox.html.custom;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.document.sandbox.html.custom.HTMLRenderer;
import com.g2forge.enigma.document.sandbox.html.custom.Span;

public class TestRenderer {
	protected final HTMLRenderer renderer = new HTMLRenderer();

	@Test
	public void contents() {
		Assert.assertEquals("<span>foo<span>bar</span></span>", renderer.render(new Span("foo", new Span("bar"))));
	}

	@Test
	public void span() {
		Assert.assertEquals("<span/>", renderer.render(new Span()));
		Assert.assertEquals("<span>foo</span>", renderer.render(new Span("foo")));
		Assert.assertEquals("<span \"id\"=\"juan\"/>", renderer.render(new Span(Collections.emptyList()).setId("juan")));
		Assert.assertEquals("<span \"id\"=\"juan\">foo</span>", renderer.render(new Span("foo").setId("juan")));
	}
}
