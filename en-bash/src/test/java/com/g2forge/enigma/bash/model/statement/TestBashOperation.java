package com.g2forge.enigma.bash.model.statement;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashOperation;

public class TestBashOperation {
	@Test
	public void and() {
		final String actual = new BashRenderer().render(new BashScript(BashOperation.Operator.And.builder().operand(new BashCommand("true")).operand(new BashCommand("true")).build()));
		HAssert.assertEquals("#!/bin/bash\ntrue && true\n", actual);
	}

	@Test
	public void line() {
		final String actual = new BashRenderer(BashRenderer.Mode.Line).render(BashOperation.Operator.Sequence.builder().operand(new BashCommand("echo", "-n", "Hello, ")).operand(new BashCommand("echo", "World!")).build());
		HAssert.assertEquals("echo -n \"Hello, \"; echo \"World!\"", actual);
	}

	@Test
	public void not() {
		final String actual = new BashRenderer(BashRenderer.Mode.Line).render(BashOperation.Operator.Parentheses.builder().operand(BashOperation.Operator.Not.builder().operand(new BashCommand("false")).build()).build());
		HAssert.assertEquals("(! false)", actual);
	}

	@Test
	public void or() {
		final String actual = new BashRenderer().render(new BashScript(BashOperation.Operator.Or.builder().operand(new BashCommand("false")).operand(new BashCommand("true")).build()));
		HAssert.assertEquals("#!/bin/bash\nfalse || true\n", actual);
	}

	@Test
	public void pipe() {
		final String actual = new BashRenderer().render(new BashScript(BashOperation.Operator.Pipe.builder().operand(new BashCommand("echo", "Hello, World!")).operand(new BashCommand("cat")).build()));
		HAssert.assertEquals("#!/bin/bash\necho \"Hello, World!\" | cat\n", actual);
	}

	@Test
	public void sequence() {
		final String actual = new BashRenderer().render(new BashScript(BashOperation.Operator.Sequence.builder().operand(new BashCommand("echo", "-n", "Hello, ")).operand(new BashCommand("echo", "World!")).build()));
		HAssert.assertEquals("#!/bin/bash\necho -n \"Hello, \"; echo \"World!\"\n", actual);
	}
}
