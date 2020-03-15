package com.g2forge.enigma.bash.convert;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashRenderer {
	@Test
	public void line() {
		HAssert.assertEquals("echo \"\\\"\"", new BashRenderer(BashRenderer.Mode.Line).render(new BashCommand("echo", "\"")));
	}
}
