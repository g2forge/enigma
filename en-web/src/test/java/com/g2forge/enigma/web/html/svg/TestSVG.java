package com.g2forge.enigma.web.html.svg;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.web.css.Block;
import com.g2forge.enigma.web.css.color.Color;
import com.g2forge.enigma.web.css.drawing.Fill;
import com.g2forge.enigma.web.css.drawing.Stroke;
import com.g2forge.enigma.web.css.drawing.StrokeWidth;
import com.g2forge.enigma.web.css.text.DominantBaseline;
import com.g2forge.enigma.web.css.text.TextAnchor;
import com.g2forge.enigma.web.html.HTMLRenderer;
import com.g2forge.enigma.web.html.html.Body;
import com.g2forge.enigma.web.html.html.HTML;

public class TestSVG {
	@Test
	public void test() {
		final Text text = new Text(50, 10, new Block(DominantBaseline.Central, TextAnchor.Middle), "Text");
		final Rectangle rectangle = new Rectangle(0, 0, 100, 20, new Block(new Fill(new Color(255, 255, 255)), new StrokeWidth(1), new Stroke(new Color(0, 0, 0))));
		final SVG svg = new SVG(200, 200, new Group(rectangle, text));
		final String actual = new HTMLRenderer().render(new HTML(null, new Body(svg)));
		Assert.assertEquals("<html><body><svg width=\"200\" height=\"200\"><g><rect x=\"0\" y=\"0\" width=\"100\" height=\"20\" style=\"fill: rgb(255,255,255); stroke-width: 1; stroke: rgb(0,0,0)\"/><text x=\"50\" y=\"10\" style=\"dominant-baseline: central; text-anchor: middle\">Text</text></g></svg></body></html>", actual);
	}
}
