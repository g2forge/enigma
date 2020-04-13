package com.g2forge.enigma.bash.model.statement;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.BashScript;

public class TestBashAssignment extends ATestBashRendering {
	@Test
	public void line() {
		HAssert.assertEquals("A=\"a a\"", toLine(new BashAssignment("A", "a a")));
	}

	@Test
	public void simple() {
		HAssert.assertEquals("#!/bin/bash\nA=a\n", toBlock(new BashScript(new BashAssignment("A", "a"))));
	}

	@Test
	public void space() {
		HAssert.assertEquals("#!/bin/bash\nA=\"a a\"\n", toBlock(new BashScript(new BashAssignment("A", "a a"))));
	}
}
