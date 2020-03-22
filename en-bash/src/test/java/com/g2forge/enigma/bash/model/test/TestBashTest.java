package com.g2forge.enigma.bash.model.test;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashIf;
import com.g2forge.enigma.bash.model.test.BashTest;
import com.g2forge.enigma.bash.model.test.BashTestOperation;

public class TestBashTest {
	@Test
	public void and() {
		test("[[ 0 -ne 1 && 1 -ge -1 ]]", new BashTest(BashTestOperation.Operator.LogicalAnd.builder().operand(BashTestOperation.Operator.NumericNotEqual.builder().operand(0).operand(1).build()).operand(BashTestOperation.Operator.NumericGreaterThanOrEqual.builder().operand(1).operand(-1).build()).build()));
	}

	@Test
	public void gt() {
		test("[[ 1 -gt 2 ]]", new BashTest(BashTestOperation.Operator.NumericGreaterThan.builder().operand(1).operand(2).build()));
	}

	@Test
	public void notLE() {
		test("[[ ! 7 -le -5 ]]", new BashTest(BashTestOperation.Operator.Not.builder().operand(BashTestOperation.Operator.NumericLessThanOrEqual.builder().operand(7).operand(-5).build()).build()));
	}

	@Test
	public void or() {
		test("[[ 0 -eq 0 ]]", new BashTest(BashTestOperation.Operator.LogicalOr.builder().operand(BashTestOperation.Operator.NumericEqual.builder().operand(0).operand(0).build()).build()));
	}

	protected void test(final String expected, final Object actual) {
		HAssert.assertEquals("if " + expected + "; then true; fi", new BashRenderer(BashRenderer.Mode.Line).render(new BashIf(actual, new BashCommand("true"))));
	}
}
