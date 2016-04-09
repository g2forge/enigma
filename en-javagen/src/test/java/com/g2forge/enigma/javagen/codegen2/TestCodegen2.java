package com.g2forge.enigma.javagen.codegen2;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

public class TestCodegen2 {
	protected static final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer('<', '>', "\n");

	@Test
	public void testClassFields() {
		Assert.assertEquals("public class MyClass {}", renderer.render(new JavaClass(null, JavaProtection.Public, "MyClass", null)));
		Assert.assertEquals("class MyClass {\n\t" + TestCodegen2.class.getName() + " a;\n}", renderer.render(new JavaClass(null, JavaProtection.Unspecified, "MyClass", Arrays.asList(new JavaField(null, JavaProtection.Unspecified, new JavaType(TestCodegen2.class), "a")))));
		Assert.assertEquals("class MyClass {\n\tprotected String foo;\n\n\tprivate int bar;\n}", renderer.render(new JavaClass(null, JavaProtection.Unspecified, "MyClass", Arrays.asList(new JavaField(null, JavaProtection.Protected, new JavaType(String.class), "foo"), new JavaField(null, JavaProtection.Private, new JavaType(Integer.TYPE), "bar")))));
	}

	@Test
	public void testMethod() {
		final JavaType string = new JavaType(String.class);
		final List<JavaAnnotation> annotations = Arrays.asList(new JavaAnnotation(string));
		Assert.assertEquals("@String\nclass MyClass {\n\t@String\n\tpublic String toString(){}\n}", renderer.render(new JavaClass(annotations, JavaProtection.Unspecified, "MyClass", Arrays.asList(new JavaMethod(annotations, JavaProtection.Public, string, "toString")))));
	}
}
