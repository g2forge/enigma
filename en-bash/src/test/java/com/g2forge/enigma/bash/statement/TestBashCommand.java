package com.g2forge.enigma.bash.statement;

import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashCommand {
	@Test
	public void line() {
		final String actual = new BashRenderer(BashRenderer.Mode.Line).render(new BashCommand("echo", "Hello, World!"));
		HAssert.assertEquals("echo \"Hello, World!\"", actual);
	}

	@Test
	public void opchars() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", "|")));
		HAssert.assertEquals("#!/bin/bash\necho |\n", actual);
	}

	@Test
	public void simple() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", "Hello, World!")));
		HAssert.assertEquals("#!/bin/bash\necho \"Hello, World!\"\n", actual);
	}

	@Test
	public void tokens() {
		HAssert.assertEquals(HCollection.asList("echo", "Hello, World!"), BashRenderer.toTokens(new BashCommand("echo", "Hello, World!")));
		HAssert.assertEquals(HCollection.asList("echo", "$(echo a)"), BashRenderer.toTokens(new BashCommand("echo", new BashCommandSubstitution(new BashCommand("echo", "a")))));
	}
}
