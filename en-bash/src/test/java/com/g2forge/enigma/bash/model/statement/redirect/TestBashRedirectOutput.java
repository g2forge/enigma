package com.g2forge.enigma.bash.model.statement.redirect;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashRedirectOutput extends ATestBashRendering {
	@Test
	public void both() {
		HAssert.assertEquals("#!/bin/bash\necho Hello &>file\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("echo", "Hello")).redirect(new BashRedirectOutput(HBashHandle.BOTH, "file")).build())));
	}

	@Test
	public void bothAppend() {
		HAssert.assertEquals("#!/bin/bash\necho Hello &>>file\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("echo", "Hello")).redirect(new BashRedirectOutput(HBashHandle.BOTH, "file", true, true)).build())));
	}

	@Test
	public void duplicate() {
		HAssert.assertEquals("#!/bin/bash\necho Hello 2>&1\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("echo", "Hello")).redirect(new BashRedirectOutput(HBashHandle.STDERR, BashRedirectHandle.Operation.Duplicate.create(HBashHandle.STDOUT))).build())));
	}

	@Test
	public void move() {
		HAssert.assertEquals("#!/bin/bash\necho Hello 2>&3-\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("echo", "Hello")).redirect(new BashRedirectOutput(HBashHandle.STDERR, BashRedirectHandle.Operation.Move.create(3))).build())));
	}

	@Test
	public void stdfile() {
		HAssert.assertEquals("#!/bin/bash\necho Hello >file\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("echo", "Hello")).redirect(new BashRedirectOutput("file")).build())));
	}

	@Test
	public void stdfileAppend() {
		HAssert.assertEquals("#!/bin/bash\necho Hello >>file\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("echo", "Hello")).redirect(new BashRedirectOutput(HBashHandle.UNSPECIFIED, "file", true, true)).build())));
	}

	@Test
	public void stdfileNoClobber() {
		HAssert.assertEquals("#!/bin/bash\necho Hello >|file\n", toBlock(new BashScript(BashRedirection.builder().executable(new BashCommand("echo", "Hello")).redirect(new BashRedirectOutput(HBashHandle.UNSPECIFIED, "file", false, false)).build())));
	}
}
