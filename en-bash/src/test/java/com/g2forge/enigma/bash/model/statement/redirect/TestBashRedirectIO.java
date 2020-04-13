package com.g2forge.enigma.bash.model.statement.redirect;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashRedirectIO extends ATestBashRendering {
	@Test
	public void handle() {
		HAssert.assertEquals("#!/bin/bash\nfake 3<>file\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("fake")).redirect(new BashRedirectIO(3, "file")).build())));
	}

	@Test
	public void std() {
		HAssert.assertEquals("#!/bin/bash\nfake <>file\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("fake")).redirect(new BashRedirectIO("file")).build())));
	}
}
