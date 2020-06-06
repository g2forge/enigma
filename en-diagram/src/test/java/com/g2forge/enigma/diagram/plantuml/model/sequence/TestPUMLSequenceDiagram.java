package com.g2forge.enigma.diagram.plantuml.model.sequence;

import org.junit.Test;

import com.g2forge.enigma.diagram.plantuml.convert.ATestPUMLRenderer;

public class TestPUMLSequenceDiagram extends ATestPUMLRenderer {
	@Test
	public void alice() {
		assertDiagram("alice", PUMLSequenceDiagram.builder().participant(new PUMLParticipant("alice")).build());
	}

	@Test
	public void empty() {
		assertDiagram("empty", PUMLSequenceDiagram.builder().build());
	}

	@Test
	public void message() {
		assertDiagram("message", PUMLSequenceDiagram.builder().participant(new PUMLParticipant("alice")).participant(new PUMLParticipant("bob")).event(new PUMLMessage("alice", "bob", "message")).build());
	}

	@Test
	public void self() {
		assertDiagram("self", PUMLSequenceDiagram.builder().participant(new PUMLParticipant("alice")).event(new PUMLMessage("alice", "alice", null)).build());
	}
}
