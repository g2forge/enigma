package com.g2forge.enigma.bash.model.expression;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashOperation;

public class TestBashProcessSubstitution extends ATestBashRendering {
	@Test
	public void io() {
		final String actual = toBlock(new BashScript(BashOperation.Operator.Pipe.builder().operand(new BashCommand("cat", BashProcessSubstitution.Direction.Input.create(new BashCommand("echo", "Hello")))).operand(new BashCommand("tee", BashProcessSubstitution.Direction.Output.create(new BashCommand("cat")))).build()));
		HAssert.assertEquals("#!/bin/bash\ncat <(echo Hello) | tee >(cat)\n", actual);
	}
}
