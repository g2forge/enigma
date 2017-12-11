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
	public static class Template {
		protected static final String TEMPLATE = "foobar<a><b><c>";

		protected String a;

		protected String b;

		public String getC() {
			return "hello";
		}
	}

	@Test
	public void test() {
		Assert.assertEquals("foobar1juanhello", EmbeddedTemplateRenderer.DEFAULT.render(new Template("1", "juan")));
	}
}
