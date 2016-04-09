package com.g2forge.enigma.stringtemplate;

import org.junit.Assert;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TestSTAttributeGenerator {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Template {
		protected static final String TEMPLATE = "<if(test)><message><endif>";

		protected boolean test;

		protected boolean called = false;

		protected String message;

		public String getMessage() {
			called = true;
			return message;
		}
	}

	@Test
	public void call() {
		final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer();
		final Template template = new Template(true, false, "foo");
		Assert.assertEquals("foo", renderer.render(template));
		Assert.assertTrue(template.isCalled());
	}

	@Test
	public void skip() {
		final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer();
		final Template template = new Template(false, false, "foo");
		Assert.assertEquals("", renderer.render(template));
		Assert.assertFalse(template.isCalled());
	}
}
