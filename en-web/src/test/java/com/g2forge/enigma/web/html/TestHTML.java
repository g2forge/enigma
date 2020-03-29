package com.g2forge.enigma.web.html;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.resource.HResource;
import com.g2forge.enigma.web.html.convert.HTMLRenderer;
import com.g2forge.enigma.web.html.html.Body;
import com.g2forge.enigma.web.html.html.Div;
import com.g2forge.enigma.web.html.html.Div.DivBuilder;
import com.g2forge.enigma.web.html.html.HTML;
import com.g2forge.enigma.web.html.html.Header;
import com.g2forge.enigma.web.html.html.Image;
import com.g2forge.enigma.web.html.html.Paragraph;
import com.g2forge.enigma.web.html.html.Span;

public class TestHTML {
	protected final HTMLRenderer renderer = new HTMLRenderer();

	@Test
	public void contents() {
		Assert.assertEquals("<span>foo<span>bar</span></span>", renderer.render(new Span(null, "foo", new Span(null, "bar"))));
	}

	@Test
	public void doc() {
		final DivBuilder div = Div.builder();
		div.element(Header.builder().level(1).content("Header").build());
		div.element(Paragraph.builder().content("Some ").content(Span.builder().content("text").build()).build());
		final HTML html = HTML.builder().body(Body.builder().element(div.build()).build()).build();

		final String actual = renderer.render(html);
		final String expected = HResource.read(getClass(), "doc.html", true);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void escape() {
		Assert.assertEquals("<span id=\"a&amp;b\">&quot;</span>", renderer.render(Span.builder().id("a&b").content("\"").build()));
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
