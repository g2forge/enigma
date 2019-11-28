package com.g2forge.enigma.backend;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.backend.bash.convert.BashRenderer;
import com.g2forge.enigma.backend.bash.model.BashCommand;
import com.g2forge.enigma.backend.bash.model.BashScript;

public class TestBash {
	@Test
	public void test() {
		final String actual = new BashRenderer().render(new BashScript(new BashCommand("echo", "Hello, World!")));
		HAssert.assertEquals("#!/bin/bash\necho \"Hello, World!\"", actual);
	}
}
