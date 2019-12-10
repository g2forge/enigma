package com.g2forge.enigma.bash.statement;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashAssignment;

public class TestBashAssignment {
	@Test
	public void simple() {
		final String actual = new BashRenderer().render(new BashScript(new BashAssignment("A", "a")));
		HAssert.assertEquals("#!/bin/bash\nA=a\n", actual);
	}
	
	@Test
	public void space() {
		final String actual = new BashRenderer().render(new BashScript(new BashAssignment("A", "a a")));
		HAssert.assertEquals("#!/bin/bash\nA=\"a a\"\n", actual);
	}
}
