package com.g2forge.enigma.bash.model.statement.redirect;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectIO;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirection;

public class TestBashRedirectIO {
	@Test
	public void handle() {
		HAssert.assertEquals("#!/bin/bash\nfake 3<>file\n", new BashRenderer().render(new BashScript(BashRedirection.builder().executable(new BashCommand("fake")).redirect(new BashRedirectIO(3, "file")).build())));
	}

	@Test
	public void std() {
		HAssert.assertEquals("#!/bin/bash\nfake <>file\n", new BashRenderer().render(new BashScript(BashRedirection.builder().executable(new BashCommand("fake")).redirect(new BashRedirectIO("file")).build())));
	}
}
