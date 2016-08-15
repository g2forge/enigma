package com.g2forge.enigma.javagen;

import java.util.EnumSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.alexandria.java.core.helpers.ResourceHelpers;
import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.core.JavaPackageSpecifier;
import com.g2forge.enigma.javagen.expression.JavaNull;
import com.g2forge.enigma.javagen.file.JavaFile;
import com.g2forge.enigma.javagen.file.JavaImport;
import com.g2forge.enigma.javagen.statement.JavaVariable;
import com.g2forge.enigma.javagen.type.JavaClass;
import com.g2forge.enigma.javagen.type.JavaField;
import com.g2forge.enigma.javagen.type.JavaMethod;
import com.g2forge.enigma.javagen.type.JavaProtection;
import com.g2forge.enigma.javagen.type.JavaType;
import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

public class TestJavagen {
	protected static final EmbeddedTemplateRenderer renderString = new EmbeddedTemplateRenderer("\n");

	protected static final EmbeddedTemplateRenderer renderFile = new EmbeddedTemplateRenderer();

	@Test
	public void testClassFields() {
		Assert.assertEquals("public class MyClass {}", renderString.render(new JavaClass(null, JavaProtection.Public, "MyClass", null)));
		Assert.assertEquals("class MyClass {\n\t" + TestJavagen.class.getName() + " a;\n}", renderString.render(new JavaClass(null, JavaProtection.Unspecified, "MyClass", CollectionHelpers.asList(new JavaField(new JavaType(TestJavagen.class), "a")))));
		Assert.assertEquals("class MyClass {\n\tprotected String foo;\n\n\tprivate int bar;\n}", renderString.render(new JavaClass(null, JavaProtection.Unspecified, "MyClass", CollectionHelpers.asList(new JavaField(null, JavaProtection.Protected, null, new JavaType(String.class), "foo", null), new JavaField(null, JavaProtection.Private, null, new JavaType(Integer.TYPE), "bar", null)))));
	}

	@Test
	public void testFile() {
		final JavaType string = new JavaType(String.class);
		final JavaMethod method = new JavaMethod(null, JavaProtection.Public, string, "toString", CollectionHelpers.asList(new JavaVariable(CollectionHelpers.asList(new JavaAnnotation(string)), EnumSet.of(JavaModifier.Final), string, "retVal", null)));
		final String actual = renderFile.render(new JavaFile(new JavaPackageSpecifier("foo"), CollectionHelpers.asList(new JavaImport(new JavaType("foo.Other"))), CollectionHelpers.asList(new JavaClass(null, JavaProtection.Unspecified, "Test", CollectionHelpers.asList(method)))));

		final String expected = ResourceHelpers.read(getClass(), "Test.java.txt");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMethod() {
		final JavaType string = new JavaType(String.class);
		final List<JavaAnnotation> annotations = CollectionHelpers.asList(new JavaAnnotation(string));
		Assert.assertEquals("@String\nclass MyClass {\n\t@String\n\tpublic String toString() {}\n}", renderString.render(new JavaClass(annotations, JavaProtection.Unspecified, "MyClass", CollectionHelpers.asList(new JavaMethod(annotations, JavaProtection.Public, string, "toString", null)))));
	}

	@Test
	public void testVariable() {
		final JavaVariable variable = new JavaVariable(new JavaType("Boolean"), "var").setInitializer(JavaNull.singleton);
		Assert.assertEquals("Boolean var = null;", renderString.render(variable));
	}
}
