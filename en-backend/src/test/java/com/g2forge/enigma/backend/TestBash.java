package com.g2forge.enigma.backend;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.backend.bash.convert.BashRenderer;
import com.g2forge.enigma.backend.bash.model.BashCommand;
import com.g2forge.enigma.backend.bash.model.BashScript;
import com.g2forge.enigma.backend.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.backend.bash.model.expression.BashString;

public class TestBash {
	@Test
	public void simple() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", "Hello, World!")));
		HAssert.assertEquals("#!/bin/bash\necho \"Hello, World!\"", actual);
	}

	@Test
	public void substitution() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", new BashCommandSubstitution(new BashCommand("echo", "Hello")))));
		HAssert.assertEquals("#!/bin/bash\necho \"$(echo Hello)\"", actual);
	}

	@Test
	public void substitutionQuoting() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", new BashString("Hello,", new BashCommandSubstitution(new BashCommand("echo", " World!"))))));
		HAssert.assertEquals("#!/bin/bash\necho \"Hello,$(echo \" World!\")\"", actual);
	}
}
