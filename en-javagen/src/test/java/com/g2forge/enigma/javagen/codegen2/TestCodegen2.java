package com.g2forge.enigma.javagen.codegen2;

import java.util.EnumSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.alexandria.test.ResourceHelpers;
import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

public class TestCodegen2 {
	protected static final EmbeddedTemplateRenderer renderString = new EmbeddedTemplateRenderer("\n");

	protected static final EmbeddedTemplateRenderer renderFile = new EmbeddedTemplateRenderer();

	@Test
	public void testClassFields() {
		Assert.assertEquals("public class MyClass {}", renderString.render(new JavaClass(null, JavaProtection.Public, "MyClass", null)));
		Assert.assertEquals("class MyClass {\n\t" + TestCodegen2.class.getName() + " a;\n}", renderString.render(new JavaClass(null, JavaProtection.Unspecified, "MyClass", CollectionHelpers.asList(new JavaField(null, JavaProtection.Unspecified, new JavaType(TestCodegen2.class), "a")))));
		Assert.assertEquals("class MyClass {\n\tprotected String foo;\n\n\tprivate int bar;\n}", renderString.render(new JavaClass(null, JavaProtection.Unspecified, "MyClass", CollectionHelpers.asList(new JavaField(null, JavaProtection.Protected, new JavaType(String.class), "foo"), new JavaField(null, JavaProtection.Private, new JavaType(Integer.TYPE), "bar")))));
	}

	@Test
	public void testFile() {
		final JavaType string = new JavaType(String.class);
		final JavaMethod method = new JavaMethod(null, JavaProtection.Public, string, "toString", CollectionHelpers.asList(new JavaVariable(CollectionHelpers.asList(new JavaAnnotation(string)), EnumSet.of(JavaModifier.Final), string, "retVal")));
		final String actual = renderFile.render(new JavaFile("foo", CollectionHelpers.asList(new JavaImport(new JavaType("foo.Other"))), CollectionHelpers.asList(new JavaClass(null, JavaProtection.Unspecified, "Test", CollectionHelpers.asList(method)))));

		final String expected = ResourceHelpers.read(getClass(), "Test.java.txt");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMethod() {
		final JavaType string = new JavaType(String.class);
		final List<JavaAnnotation> annotations = CollectionHelpers.asList(new JavaAnnotation(string));
		Assert.assertEquals("@String\nclass MyClass {\n\t@String\n\tpublic String toString(){}\n}", renderString.render(new JavaClass(annotations, JavaProtection.Unspecified, "MyClass", CollectionHelpers.asList(new JavaMethod(annotations, JavaProtection.Public, string, "toString", null)))));
	}
}
