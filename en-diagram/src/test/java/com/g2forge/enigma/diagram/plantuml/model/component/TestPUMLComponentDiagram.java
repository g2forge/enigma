package com.g2forge.enigma.diagram.plantuml.model.component;

import org.junit.Test;

import com.g2forge.enigma.diagram.plantuml.convert.ATestPUMLRenderer;

public class TestPUMLComponentDiagram extends ATestPUMLRenderer {
	@Test
	public void empty() {
		assertDiagram("empty", PUMLComponentDiagram.builder().build());
	}

	@Test
	public void mangle() {
		assertDiagram("mangle", PUMLComponentDiagram.builder().component(new PUMLComponent("A")).component(new PUMLComponent("B Extended")).link(PUMLLink.builder("A", "B Extended").modifier(PUMLLink.Modifier.Hidden).build()).build());
	}

	@Test
	public void one() {
		assertDiagram("one", PUMLComponentDiagram.builder().component(new PUMLComponent("A")).build());
	}

	@Test
	public void two() {
		assertDiagram("two", PUMLComponentDiagram.builder().component(new PUMLComponent("A")).component(new PUMLComponent("B")).link(PUMLLink.builder("A", "B").vertical(true).build()).build());
	}
}
