package com.g2forge.enigma.bash.model.statement;

import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.bash.model.expression.BashString;

public class TestBashCommand extends ATestBashRendering {
	@Test
	public void bashEchoFoo() {
		HAssert.assertEquals(HCollection.asList("bash", "-c", "echo foo"), BashRenderer.toTokens(new BashCommand("bash", "-c", new BashCommand("echo", "foo"))));
	}

	@Test
	public void bashEchoOpchar() {
		HAssert.assertEquals(HCollection.asList("bash", "-c", "echo \"&\""), BashRenderer.toTokens(new BashCommand("bash", "-c", new BashCommand("echo", new BashString("&")))));
	}

	@Test
	public void line() {
		HAssert.assertEquals("echo \"Hello, World!\"", toLine(new BashCommand("echo", "Hello, World!")));
	}

	@Test
	public void opchars() {
		HAssert.assertEquals("#!/bin/bash\necho \"|\"\n", toBlock(new BashScript(new BashCommand("echo", "|"))));
	}

	@Test
	public void simple() {
		HAssert.assertEquals("#!/bin/bash\necho \"Hello, World!\"\n", toBlock(new BashScript(new BashCommand("echo", "Hello, World!"))));
	}

	@Test
	public void tokens() {
		HAssert.assertEquals(HCollection.asList("echo", "Hello, World!"), toTokens(new BashCommand("echo", "Hello, World!")));
	}

	@Test
	public void variable() {
		HAssert.assertEquals(HCollection.asList("echo", "$(echo a)"), toTokens(new BashCommand("echo", new BashCommandSubstitution(new BashCommand("echo", "a")))));
	}
}
