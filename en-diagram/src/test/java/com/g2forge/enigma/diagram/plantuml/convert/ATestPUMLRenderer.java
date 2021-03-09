package com.g2forge.enigma.diagram.plantuml.convert;

import com.g2forge.alexandria.java.core.resource.Resource;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.diagram.plantuml.model.IPUMLDiagram;

public abstract class ATestPUMLRenderer {
	protected void assertDiagram(final String name, final IPUMLDiagram diagram) {
		HAssert.assertEquals(new Resource(getClass(), name + ".puml"), new PUMLRenderer().render(diagram));
	}
}
