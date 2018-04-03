package com.g2forge.enigma.javagen;

import java.util.EnumSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HResource;
import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.core.JavaPackageSpecifier;
import com.g2forge.enigma.javagen.file.JavaFile;
import com.g2forge.enigma.javagen.file.JavaImport;
import com.g2forge.enigma.javagen.imperative.expression.JavaNull;
import com.g2forge.enigma.javagen.imperative.statement.JavaBlock;
import com.g2forge.enigma.javagen.imperative.statement.JavaVariable;
import com.g2forge.enigma.javagen.type.decl.JavaElement;
import com.g2forge.enigma.javagen.type.decl.JavaField;
import com.g2forge.enigma.javagen.type.decl.JavaMetaType;
import com.g2forge.enigma.javagen.type.decl.JavaMethod;
import com.g2forge.enigma.javagen.type.decl.JavaProtection;
import com.g2forge.enigma.javagen.type.decl.JavaTypeDeclaration;
import com.g2forge.enigma.javagen.type.expression.JavaType;

public class TestJavagen extends ATestJavagen {
	@Test
	public void testClassFields() {
		Assert.assertEquals("public class MyClass {}", renderString.render(JavaTypeDeclaration.standardBuilder().name("MyClass").build()));
		Assert.assertEquals("class MyClass {\n\t" + TestJavagen.class.getName() + " a;\n}", renderString.render(JavaTypeDeclaration.builder().protection(JavaProtection.Unspecified).meta(JavaMetaType.Class).name("MyClass").member(JavaField.builder().type(new JavaType(TestJavagen.class)).name("a").build()).build()));
		Assert.assertEquals("class MyClass {\n\tprotected String foo;\n\n\tprivate int bar;\n}", renderString.render(JavaTypeDeclaration.builder().protection(JavaProtection.Unspecified).meta(JavaMetaType.Class).name("MyClass").member(JavaField.builder().protection(JavaProtection.Protected).type(new JavaType(String.class)).name("foo").build()).member(new JavaField(null, JavaProtection.Private, null, new JavaType(Integer.TYPE), "bar", null)).build()));
	}

	@Test
	public void testEnum() {
		Assert.assertEquals("enum MyEnum {}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, null, null)));
		Assert.assertEquals("enum MyEnum {\n\tElement\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, HCollection.asList(new JavaElement("Element")), null)));
		Assert.assertEquals("enum MyEnum {\n\tElement;\n\n\tFoo foo;\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, HCollection.asList(new JavaElement("Element")), HCollection.asList(JavaField.builder().type(new JavaType("Foo")).name("foo").build()))));
		Assert.assertEquals("enum MyEnum {\n\t;\n\n\tFoo foo;\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, null, HCollection.asList(JavaField.builder().type(new JavaType("Foo")).name("foo").build()))));
	}

	@Test
	public void testInnerInterface() {
		Assert.assertEquals("class MyClass {\n\tstatic interface MyInterface {}\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, "MyClass", null, null, null, HCollection.asList(new JavaTypeDeclaration(null, JavaProtection.Unspecified, EnumSet.of(JavaModifier.Static), JavaMetaType.Interface, "MyInterface", null, null, null, null)))));
	}

	@Test
	public void testParents() {
		Assert.assertEquals("public class MyClass extends String {}", renderString.render(JavaTypeDeclaration.standardBuilder().name("MyClass").superclass(new JavaType(String.class)).build()));
		Assert.assertEquals("public class MyClass extends String implements ISomething {}", renderString.render(JavaTypeDeclaration.standardBuilder().name("MyClass").superclass(new JavaType(String.class)).superinterface(new JavaType("ISomething")).build()));
		Assert.assertEquals("public class MyClass implements ISomething {}", renderString.render(JavaTypeDeclaration.standardBuilder().name("MyClass").superinterface(new JavaType("ISomething")).build()));
		Assert.assertEquals("public interface MyInterface extends ISomething0, ISomething1 {}", renderString.render(JavaTypeDeclaration.standardBuilder().meta(JavaMetaType.Interface).name("MyInterface").superinterface(new JavaType("ISomething0")).superinterface(new JavaType("ISomething1")).build()));
	}

	@Test
	public void testFile() {
		final JavaType string = new JavaType(String.class);
		final JavaMethod method = JavaMethod.standardBuilder().type(string).name("toString").body(JavaBlock.builder().statement(JavaVariable.standardBuilder().type(string).name("retVal").annotation(new JavaAnnotation(string)).build()).build()).build();
		final String actual = renderFile.render(new JavaFile(new JavaPackageSpecifier("foo"), HCollection.asList(new JavaImport(new JavaType("foo.Other"))), HCollection.asList(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, "Test", null, null, null, HCollection.asList(method)))));

		final String expected = HResource.read(getClass(), "Test.java.txt", false);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMethod() {
		final JavaType string = new JavaType(String.class);
		final List<JavaAnnotation> annotations = HCollection.asList(new JavaAnnotation(string));
		Assert.assertEquals("@String\nclass MyClass {\n\t@String\n\tpublic String toString() {}\n}", renderString.render(new JavaTypeDeclaration(annotations, JavaProtection.Unspecified, null, JavaMetaType.Class, "MyClass", null, null, null, HCollection.asList(JavaMethod.standardBuilder().type(string).name("toString").annotation(new JavaAnnotation(string)).body(new JavaBlock(null)).build()))));
		Assert.assertEquals("interface MyInterface {\n\tpublic String toString();\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Interface, "MyInterface", null, null, null, HCollection.asList(JavaMethod.standardBuilder().type(string).name("toString").build()))));
	}

	@Test
	public void testVariable() {
		final JavaVariable variable = JavaVariable.standardBuilder().type(new JavaType("Boolean")).name("var").initializer(JavaNull.singleton).build();
		Assert.assertEquals("final Boolean var = null;", renderString.render(variable));
	}
}
