package com.g2forge.enigma.bash.expression;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.bash.model.expression.BashString;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashIf;

public class TestBashCommandSubstitution {
	@Test
	public void complexQuoting() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", new BashString("Hello,", new BashCommandSubstitution(new BashCommand("echo", " World!"))))));
		HAssert.assertEquals("#!/bin/bash\necho \"Hello,$(echo \" World!\")\"\n", actual);
	}

	@Test
	public void conditional() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", new BashCommandSubstitution(new BashIf("true", new BashCommand("echo", "0"), new BashCommand("echo", "1"))))));
		HAssert.assertEquals("#!/bin/bash\necho \"$(if true; then echo 0; else echo 1; fi)\"\n", actual);
	}

	@Test
	public void simple() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", new BashCommandSubstitution(new BashCommand("echo", "Hello")))));
		HAssert.assertEquals("#!/bin/bash\necho \"$(echo Hello)\"\n", actual);
	}
}
