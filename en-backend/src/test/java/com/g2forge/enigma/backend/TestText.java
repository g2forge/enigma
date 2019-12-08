package com.g2forge.enigma.backend;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.backend.convert.TextRenderer;

public class TestText {
	@Test
	public void test() {
		final String string = "Hello, World!";
		HAssert.assertEquals(string, new TextRenderer().render(string));
	}
}
