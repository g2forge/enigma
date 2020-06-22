package com.g2forge.enigma.diagram.dot.convert;

import com.g2forge.alexandria.java.core.resource.Resource;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.diagram.dot.model.DotGraph;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class ATestDotRenderer {
	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private static final DotRenderer renderer = new DotRenderer();
	
	protected void assertDot(final String name, final DotGraph graph) {
		HAssert.assertEquals(new Resource(getClass(), name + ".dot"), getRenderer().render(graph));
	}
}
