package com.g2forge.enigma.diagram.dot.model;

import org.junit.Test;

import com.g2forge.enigma.diagram.dot.convert.ATestDotRenderer;
import com.g2forge.enigma.diagram.dot.model.DotGraph.DotGraphBuilder;

public class TestDotGraph extends ATestDotRenderer {
	@Test
	public void attributes() {
		final DotGraphBuilder builder = DotGraph.builder().directed(false).name("attributes");
		builder.attribute$("size", "1,1");
		builder.statement(DotNode.builder().name("a").attribute$("label", "Foo").build());
		builder.statement(DotNode.builder().name("b").attribute$("shape", "box").build());
		builder.statement(DotEdge.builder().node("a").node("b").node("c").attribute$("color", "blue").build());
		builder.statement(DotEdge.builder().node("b").node("d").attribute$("style", "dotted").build());
		assertDot("attributes", builder.build());
	}

	@Test
	public void digraph() {
		assertDot("digraph", DotGraph.builder().directed(true).name("digraph").statement(DotEdge.builder().node("a").node("b").node("c").build()).statement(DotEdge.builder().node("b").node("d").build()).build());
	}

	@Test
	public void empty() {
		assertDot("empty", DotGraph.builder().name("empty").build());
	}

	@Test
	public void graph() {
		assertDot("graph", DotGraph.builder().directed(false).name("graph").statement(DotEdge.builder().node("a").node("b").node("c").build()).statement(DotEdge.builder().node("b").node("d").build()).build());
	}

	@Test
	public void node() {
		assertDot("node", DotGraph.builder().directed(true).name("node").statement(new DotNode("a")).build());
	}
}
