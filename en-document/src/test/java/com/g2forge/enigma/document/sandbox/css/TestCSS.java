package com.g2forge.enigma.document.sandbox.css;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.document.sandbox.css.distance.Distance;
import com.g2forge.enigma.document.sandbox.css.distance.Distance.Unit;
import com.g2forge.enigma.document.sandbox.css.layout.Display;
import com.g2forge.enigma.document.sandbox.css.layout.Margin;
import com.g2forge.enigma.document.sandbox.css.layout.MarginLeft;
import com.g2forge.enigma.document.sandbox.css.layout.MarginRight;
import com.g2forge.enigma.document.sandbox.css.layout.MaxHeight;
import com.g2forge.enigma.document.sandbox.css.layout.MaxWidth;
import com.g2forge.enigma.document.sandbox.css.text.TextAnchor;

public class TestCSS {
	@Test
	public void complex() {
		final String actual = new CSSRenderer().render(new Block(new MarginLeft(Margin.Auto), new MarginRight(Margin.Auto), Display.Block, new MaxWidth(new Distance(500, Unit.PX)), new MaxHeight(new Distance(500, Unit.PX))));
		Assert.assertEquals("margin-left: auto; margin-right: auto; display: block; max-width: 500px; max-height: 500px", actual);
	}

	@Test
	public void enumeration() {
		Assert.assertEquals("text-anchor: middle", new CSSRenderer().render(TextAnchor.Middle));
	}
}
