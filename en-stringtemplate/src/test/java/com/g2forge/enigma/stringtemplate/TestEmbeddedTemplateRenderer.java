package com.g2forge.enigma.stringtemplate;

import org.junit.Assert;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TestEmbeddedTemplateRenderer {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TestTemplate {
		public static final String TEMPLATE = "foobar<a><b>";

		protected String a;

		protected String b;
	}

	@Test
	public void test() {
		final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer();
		Assert.assertEquals("foobar1juan", renderer.render(new TestTemplate("1", "juan")));
	}
}
