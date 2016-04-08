package com.g2forge.enigma.javagen.codegen2;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

public class TestCodegen2 {
	protected static final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer('<', '>', "\n");

	@Test
	public void testClassFields() {
		Assert.assertEquals("class MyClass {}", renderer.render(new JavaClass("MyClass", null)));
		Assert.assertEquals("class MyClass {\n\tMyClass a;\n}", renderer.render(new JavaClass("MyClass", Arrays.asList(new JavaField("MyClass", "a")))));
		Assert.assertEquals("class MyClass {\n\tString foo;\n\n\tint bar;\n}", renderer.render(new JavaClass("MyClass", Arrays.asList(new JavaField("String", "foo"), new JavaField("int", "bar")))));
	}
}
