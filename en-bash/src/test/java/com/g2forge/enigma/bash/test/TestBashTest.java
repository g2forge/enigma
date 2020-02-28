package com.g2forge.enigma.bash.test;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashIf;
import com.g2forge.enigma.bash.model.test.BashTest;
import com.g2forge.enigma.bash.model.test.BashTestOperation;

public class TestBashTest {
	@Test
	public void gt() {
		final String actual = new BashRenderer(BashRenderer.Mode.Line).render(new BashIf(new BashTest(BashTestOperation.Operator.NumericGreaterThan.createOperation().operand(1).operand(2).build()), new BashCommand("true")));
		HAssert.assertEquals("if [[ 1 > 2 ]]; then true; fi", actual);
	}
}
