package com.g2forge.enigma.web.css;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.web.css.convert.CSSRenderer;
import com.g2forge.enigma.web.css.distance.Distance;
import com.g2forge.enigma.web.css.distance.Distance.Unit;
import com.g2forge.enigma.web.css.layout.Display;
import com.g2forge.enigma.web.css.layout.Margin;
import com.g2forge.enigma.web.css.layout.MarginLeft;
import com.g2forge.enigma.web.css.layout.MarginRight;
import com.g2forge.enigma.web.css.layout.MaxHeight;
import com.g2forge.enigma.web.css.layout.MaxWidth;
import com.g2forge.enigma.web.css.text.TextAnchor;

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
