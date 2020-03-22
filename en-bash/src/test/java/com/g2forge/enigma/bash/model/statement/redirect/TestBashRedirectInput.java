package com.g2forge.enigma.bash.model.statement.redirect;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectHandle;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectInput;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirection;

public class TestBashRedirectInput {
	@Test
	public void duplicate() {
		HAssert.assertEquals("#!/bin/bash\ncat <&3\n", new BashRenderer().render(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectInput(BashRedirectHandle.Operation.Duplicate.create(3))).build())));
	}

	@Test
	public void move() {
		HAssert.assertEquals("#!/bin/bash\ncat <&3-\n", new BashRenderer().render(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectInput(BashRedirectHandle.Operation.Move.create(3))).build())));
	}

	@Test
	public void otherfile() {
		HAssert.assertEquals("#!/bin/bash\ncat 3<file\n", new BashRenderer().render(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectInput(3, "file")).build())));
	}

	@Test
	public void stdfile() {
		HAssert.assertEquals("#!/bin/bash\ncat <file\n", new BashRenderer().render(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectInput("file")).build())));
	}
}
