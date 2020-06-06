package com.g2forge.enigma.diagram.plantuml.model.klass;

import org.junit.Test;

import com.g2forge.enigma.diagram.plantuml.convert.ATestPUMLRenderer;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClassDiagram.PUMLClassDiagramBuilder;
import com.g2forge.enigma.diagram.plantuml.model.style.StringPUMLColor;

public class TestPUMLClassDiagram extends ATestPUMLRenderer {
	@Test
	public void empty() {
		assertDiagram("empty", PUMLClassDiagram.builder().build());
	}

	@Test
	public void klass() {
		assertDiagram("klass", PUMLClassDiagram.builder().uclass(PUMLClass.builder().name("A").build()).build());
	}

	@Test
	public void member() {
		assertDiagram("member", PUMLClassDiagram.builder().uclass(PUMLClass.builder().name("A").member("member").build()).build());
	}

	@Test
	public void members() {
		assertDiagram("members", PUMLClassDiagram.builder().uclass(PUMLClass.builder().name("A").member("zero").member("one").build()).build());
	}

	@Test
	public void relation() {
		final PUMLClassDiagramBuilder builder = PUMLClassDiagram.builder();
		builder.uclass(PUMLClass.builder().name("Car").member("Wheel[] wheels").build());
		builder.uclass(PUMLClass.builder().name("Tesla").build());
		builder.uclass(PUMLClass.builder().name("Wheel").build());
		builder.relation(PUMLRelation.builder().left("Car").right("Tesla").type(PUMLRelation.Type.Extension).vertical(true).back(true).build());
		builder.relation(PUMLRelation.builder().left("Car").right("Wheel").type(PUMLRelation.Type.Composition).arrow(PUMLRelation.Direction.Right).label("wheels").rightLabel("0..n").back(true).build());
		assertDiagram("relation", builder.build());
	}

	@Test
	public void stereotypes() {
		final PUMLClassDiagramBuilder builder = PUMLClassDiagram.builder();
		builder.uclass(PUMLClass.builder().name("Object").stereotypeNamed("general").build());
		builder.uclass(PUMLClass.builder().name("System").stereotypeSpot('S', new StringPUMLColor("#FF7700")).stereotypeNamed("Singleton").build());
		builder.uclass(PUMLClass.builder().name("Date").stereotypeSpot('D', new StringPUMLColor("orchid")).build());
		assertDiagram("stereotypes", builder.build());
	}
}
