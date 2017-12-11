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
import com.g2forge.enigma.javagen.type.JavaElement;
import com.g2forge.enigma.javagen.type.JavaField;
import com.g2forge.enigma.javagen.type.JavaMetaType;
import com.g2forge.enigma.javagen.type.JavaMethod;
import com.g2forge.enigma.javagen.type.JavaProtection;
import com.g2forge.enigma.javagen.type.JavaType;
import com.g2forge.enigma.javagen.type.JavaTypeDeclaration;
import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

public class TestJavagen {
	protected static final EmbeddedTemplateRenderer renderString = new EmbeddedTemplateRenderer("\n");

	protected static final EmbeddedTemplateRenderer renderFile = new EmbeddedTemplateRenderer();

	@Test
	public void testClassFields() {
		final JavaTypeDeclaration base = new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, null, null, null);

		Assert.assertEquals("public class MyClass {}", renderString.render(new JavaTypeDeclaration("MyClass")));
		Assert.assertEquals("class MyClass {\n\t" + TestJavagen.class.getName() + " a;\n}", renderString.render(new JavaTypeDeclaration(base, "MyClass").setMembers(CollectionHelpers.asList(new JavaField(new JavaType(TestJavagen.class), "a")))));
		Assert.assertEquals("class MyClass {\n\tprotected String foo;\n\n\tprivate int bar;\n}", renderString.render(new JavaTypeDeclaration(base, "MyClass").setMembers(CollectionHelpers.asList(new JavaField(null, JavaProtection.Protected, null, new JavaType(String.class), "foo", null), new JavaField(null, JavaProtection.Private, null, new JavaType(Integer.TYPE), "bar", null)))));
	}

	@Test
	public void testEnum() {
		Assert.assertEquals("enum MyEnum {}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null)));
		Assert.assertEquals("enum MyEnum {\n\tElement\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", CollectionHelpers.asList(new JavaElement("Element")), null)));
		Assert.assertEquals("enum MyEnum {\n\tElement;\n\n\tFoo foo;\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", CollectionHelpers.asList(new JavaElement("Element")), CollectionHelpers.asList(new JavaField(new JavaType("Foo"), "foo")))));
		Assert.assertEquals("enum MyEnum {\n\t;\n\n\tFoo foo;\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, CollectionHelpers.asList(new JavaField(new JavaType("Foo"), "foo")))));
	}

	@Test
	public void testInnerInterface() {
		Assert.assertEquals("class MyClass {\n\tstatic interface MyInterface {}\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, "MyClass", null, CollectionHelpers.asList(new JavaTypeDeclaration(null, JavaProtection.Unspecified, EnumSet.of(JavaModifier.Static), JavaMetaType.Interface, "MyInterface", null, null)))));
	}

	@Test
	public void testFile() {
		final JavaType string = new JavaType(String.class);
		final JavaMethod method = new JavaMethod(string, "toString").setStatements(CollectionHelpers.asList(new JavaVariable(JavaVariable.STANDARD, string, "retVal").setAnnotations(CollectionHelpers.asList(new JavaAnnotation(string)))));
		final String actual = renderFile.render(new JavaFile(new JavaPackageSpecifier("foo"), CollectionHelpers.asList(new JavaImport(new JavaType("foo.Other"))), CollectionHelpers.asList(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, "Test", null, CollectionHelpers.asList(method)))));

		final String expected = ResourceHelpers.read(getClass(), "Test.java.txt");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMethod() {
		final JavaType string = new JavaType(String.class);
		final List<JavaAnnotation> annotations = CollectionHelpers.asList(new JavaAnnotation(string));
		Assert.assertEquals("@String\nclass MyClass {\n\t@String\n\tpublic String toString() {}\n}", renderString.render(new JavaTypeDeclaration(annotations, JavaProtection.Unspecified, null, JavaMetaType.Class, "MyClass", null, CollectionHelpers.asList(new JavaMethod(string, "toString").setAnnotations(annotations).setStatements(CollectionHelpers.getEmpty())))));
		Assert.assertEquals("interface MyInterface {\n\tpublic String toString();\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Interface, "MyInterface", null, CollectionHelpers.asList(new JavaMethod(string, "toString")))));
	}

	@Test
	public void testVariable() {
		final JavaVariable variable = new JavaVariable(new JavaType("Boolean"), "var").setInitializer(JavaNull.singleton);
		Assert.assertEquals("final Boolean var = null;", renderString.render(variable));
	}
}
