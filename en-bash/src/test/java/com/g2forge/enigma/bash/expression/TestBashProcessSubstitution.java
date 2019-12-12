package com.g2forge.enigma.bash.expression;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashProcessSubstitution;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashOperation;

public class TestBashProcessSubstitution {
	@Test
	public void io() {
		final String actual = new BashRenderer().render(new BashScript(BashOperation.Operator.Pipe.builder().operand(new BashCommand("cat", BashProcessSubstitution.Direction.Input.create(new BashCommand("echo", "Hello")))).operand(new BashCommand("tee", BashProcessSubstitution.Direction.Output.create(new BashCommand("cat")))).build()));
		HAssert.assertEquals("#!/bin/bash\ncat <(echo Hello) | tee >(cat)\n", actual);
	}
}
