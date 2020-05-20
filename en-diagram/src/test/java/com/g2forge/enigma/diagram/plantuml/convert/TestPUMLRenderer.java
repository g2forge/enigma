package com.g2forge.enigma.diagram.plantuml.convert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import com.g2forge.alexandria.java.io.file.TempDirectory;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClass;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClassDiagram;

public class TestPUMLRenderer {
	@Test
	public void render() throws FileNotFoundException, IOException {
		try (final TempDirectory temp = new TempDirectory()) {
			new PUMLRenderer().toPNG(PUMLClassDiagram.builder().uclass(PUMLClass.builder().name("A").build()).build(), temp.get().resolve("diagram"));
			HAssert.assertTrue(Files.exists(temp.get().resolve("diagram.png")));
		}
	}
}
