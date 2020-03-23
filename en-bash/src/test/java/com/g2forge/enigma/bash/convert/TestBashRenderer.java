package com.g2forge.enigma.bash.convert;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.bash.model.expression.BashExpansion;
import com.g2forge.enigma.bash.model.expression.BashString;
import com.g2forge.enigma.bash.model.statement.BashAssignment;
import com.g2forge.enigma.bash.model.statement.BashBlank;
import com.g2forge.enigma.bash.model.statement.BashBlock;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashOperation;

public class TestBashRenderer {
	@Test
	public void line() {
		HAssert.assertEquals("echo \"\\\"\"", new BashRenderer(BashRenderer.Mode.Line).render(new BashCommand("echo", "\"")));
	}

	@Test
	public void header() {
		final BashBlock.BashBlockBuilder block = BashBlock.builder();
		block.content(BashBlank.create());
		block.content(new BashAssignment("SELF_DIR", new BashCommandSubstitution(BashOperation.Operator.And.builder().operand(new BashCommand("cd", new BashCommandSubstitution(new BashCommand("dirname", new BashExpansion("0"))))).operand(new BashCommand("pwd", "-P")).build())));
		block.content(new BashAssignment("SELF", new BashString(new BashExpansion("SELF_DIR"), "/", new BashCommandSubstitution(new BashCommand("basename", new BashExpansion("0"))))));
		final String actual = new BashRenderer().render(new BashScript(block.build()));
		HAssert.assertEquals("#!/bin/bash\n\nSELF_DIR=\"$(cd \"$(dirname \"${0}\")\" && pwd -P)\"\nSELF=\"${SELF_DIR}/$(basename \"${0}\")\"\n", actual);
	}
}
