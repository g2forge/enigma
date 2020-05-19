package com.g2forge.enigma.diagram.plantuml.model.component;

import org.junit.Test;

import com.g2forge.alexandria.java.core.resource.Resource;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.diagram.plantuml.convert.PUMLRenderer;

public class TestPUMLComponentDiagram {
	protected void assertDiagram(final String name, final PUMLComponentDiagram diagram) {
		HAssert.assertEquals(new Resource(getClass(), name + ".puml"), new PUMLRenderer().render(diagram));
	}

	@Test
	public void empty() {
		assertDiagram("empty", PUMLComponentDiagram.builder().build());
	}
	
	@Test
	public void one() {
		assertDiagram("one", PUMLComponentDiagram.builder().component(new PUMLComponent("A")).build());
	}
	
	@Test
	public void two() {
		assertDiagram("two", PUMLComponentDiagram.builder().component(new PUMLComponent("A")).component(new PUMLComponent("B")).link(PUMLLink.builder().left("A").right("B").vertical(true).build()).build());
	}
	
	@Test
	public void mangle() {
		assertDiagram("mangle", PUMLComponentDiagram.builder().component(new PUMLComponent("A")).component(new PUMLComponent("B Extended")).link(PUMLLink.builder().left("A").right("B Extended").modifier(PUMLLink.Modifier.Hidden).build()).build());
	}
}
