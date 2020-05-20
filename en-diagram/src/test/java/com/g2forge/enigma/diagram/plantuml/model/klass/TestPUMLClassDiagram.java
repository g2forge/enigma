package com.g2forge.enigma.diagram.plantuml.model.klass;

import org.junit.Test;

import com.g2forge.enigma.diagram.plantuml.model.ATestPUMLDiagram;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClassDiagram.PUMLClassDiagramBuilder;

public class TestPUMLClassDiagram extends ATestPUMLDiagram {
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
		builder.uclass(PUMLClass.builder().name("System").stereotypeSpot('S', "#FF7700").stereotypeNamed("Singleton").build());
		builder.uclass(PUMLClass.builder().name("Date").stereotypeSpot('D', "orchid").build());
		assertDiagram("stereotypes", builder.build());
	}
}
