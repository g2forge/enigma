package com.g2forge.enigma.bash.model.expression;

import org.junit.Test;

import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.alexandria.java.text.quote.QuoteControl;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.model.ATestBashRendering;
import com.g2forge.enigma.bash.model.statement.BashCommand;

public class TestBashString extends ATestBashRendering {
	@Test
	public void control() {
		HAssert.assertEquals("echo \"&\"", toLine(new BashCommand("echo", new BashString("&"))));
	}

	@Test
	public void doubleExpand() {
		HAssert.assertEquals("echo \"${X}\"", toLine(new BashCommand("echo", new BashString(BashQuoteType.BashDoubleExpand, QuoteControl.Always, "${X}"))));
	}

	@Test
	public void doubleNoExpand() {
		HAssert.assertEquals("echo \"\\${X}\"", toLine(new BashCommand("echo", new BashString(BashQuoteType.BashDoubleNoExpand, QuoteControl.Always, "${X}"))));
	}

	@Test
	public void single() {
		HAssert.assertEquals("echo 'a'", toLine(new BashCommand("echo", new BashString(BashQuoteType.BashSingle, QuoteControl.Always, "a"))));
	}

	@Test
	public void unspecified() {
		HAssert.assertEquals("echo \"Hello, World!\"", toLine(new BashCommand("echo", new BashString("Hello, World!"))));
	}
}
