package com.g2forge.enigma.bash.statement;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashBlock;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashIf;

public class TestBashIf {
	@Test
	public void thenSimple() {
		final String actual = new BashRenderer().render(new BashScript(new BashIf("true", new BashCommand("echo", "Hello, World!"))));
		HAssert.assertEquals("#!/bin/bash\nif true; then\n\techo \"Hello, World!\"\nfi\n", actual);
	}

	@Test
	public void thenBlock() {
		final String actual = new BashRenderer().render(new BashScript(new BashIf("true", new BashBlock(new BashCommand("echo", "0"), new BashCommand("echo", "1")))));
		HAssert.assertEquals("#!/bin/bash\nif true; then\n\techo 0\n\techo 1\nfi\n", actual);
	}

	@Test
	public void thenElse() {
		final String actual = new BashRenderer().render(new BashScript(new BashIf("false", new BashCommand("echo", "true"), new BashCommand("echo", "false"))));
		HAssert.assertEquals("#!/bin/bash\nif false; then\n\techo true\nelse\n\techo false\nfi\n", actual);
	}
}
