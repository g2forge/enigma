package com.g2forge.enigma.javagen.codegen2;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

public class TestCodegen2 {
	protected static final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer('<', '>', "\n");

	@Test
	public void testClassFields() {
		Assert.assertEquals("public class MyClass {}", renderer.render(new JavaClass(JavaProtection.Public, "MyClass", null)));
		Assert.assertEquals("class MyClass {\n\t" + TestCodegen2.class.getName() + " a;\n}", renderer.render(new JavaClass(JavaProtection.Unspecified, "MyClass", Arrays.asList(new JavaField(JavaProtection.Unspecified, new JavaType(TestCodegen2.class), "a")))));
		Assert.assertEquals("class MyClass {\n\tprotected String foo;\n\n\tprivate int bar;\n}", renderer.render(new JavaClass(JavaProtection.Unspecified, "MyClass", Arrays.asList(new JavaField(JavaProtection.Protected, new JavaType(String.class), "foo"), new JavaField(JavaProtection.Private, new JavaType(Integer.TYPE), "bar")))));
	}

	@Test
	public void testMethod() {
		Assert.assertEquals("class MyClass {\n\tpublic String toString(){}\n}", renderer.render(new JavaClass(JavaProtection.Unspecified, "MyClass", Arrays.asList(new JavaMethod(JavaProtection.Public, new JavaType(String.class), "toString")))));
	}
}
