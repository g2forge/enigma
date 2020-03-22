package com.g2forge.enigma.bash.model.expression;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashExpansion;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashExpansion {
	@Test
	public void simple() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", new BashExpansion("A"))));
		HAssert.assertEquals("#!/bin/bash\necho \"${A}\"\n", actual);
	}
}
