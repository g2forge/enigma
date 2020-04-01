package com.g2forge.enigma.bash.model.statement.redirect;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashRedirectHereDoc extends ATestBashRendering {
	@Test
	public void basic() {
		HAssert.assertEquals("#!/bin/bash\ncat <<EOF\n\tText\nEOF\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectHereDoc("\tText")).build())));
	}

	@Test
	public void noexpand() {
		HAssert.assertEquals("#!/bin/bash\ncat <<\"DELIM\"\n\tText\nDELIM\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectHereDoc(HBashHandle.UNSPECIFIED, "DELIM", "\tText", false, false)).build())));
	}

	@Test
	public void striptabs() {
		HAssert.assertEquals("#!/bin/bash\ncat 3<<-EOF\n\tText\nEOF\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("cat")).redirect(new BashRedirectHereDoc(3, "EOF", "\tText", true, true)).build())));
	}
}
