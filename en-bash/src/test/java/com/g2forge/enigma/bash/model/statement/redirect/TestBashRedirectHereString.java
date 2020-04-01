package com.g2forge.enigma.bash.model.statement.redirect;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashRedirectHereString extends ATestBashRendering {
	@Test
	public void other() {
		HAssert.assertEquals("#!/bin/bash\ncat 3<<< Hello\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectHereString(3, "Hello")).build())));
	}

	@Test
	public void std() {
		HAssert.assertEquals("#!/bin/bash\ncat <<< Hello\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectHereString("Hello")).build())));
	}
}
