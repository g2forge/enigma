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
import com.g2forge.enigma.javagen.file.JavaFile;
import com.g2forge.enigma.javagen.file.JavaImport;
import com.g2forge.enigma.javagen.imperative.expression.JavaNull;
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
		final JavaTypeDeclaration base = new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, null, null, null, null, null);

		Assert.assertEquals("public class MyClass {}", renderString.render(new JavaTypeDeclaration("MyClass")));
		Assert.assertEquals("class MyClass {\n\t" + TestJavagen.class.getName() + " a;\n}", renderString.render(new JavaTypeDeclaration(base, "MyClass").setMembers(CollectionHelpers.asList(new JavaField(new JavaType(TestJavagen.class), "a")))));
		Assert.assertEquals("class MyClass {\n\tprotected String foo;\n\n\tprivate int bar;\n}", renderString.render(new JavaTypeDeclaration(base, "MyClass").setMembers(CollectionHelpers.asList(new JavaField(null, JavaProtection.Protected, null, new JavaType(String.class), "foo", null), new JavaField(null, JavaProtection.Private, null, new JavaType(Integer.TYPE), "bar", null)))));
	}

	@Test
	public void testEnum() {
		Assert.assertEquals("enum MyEnum {}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, null, null)));
		Assert.assertEquals("enum MyEnum {\n\tElement\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, CollectionHelpers.asList(new JavaElement("Element")), null)));
		Assert.assertEquals("enum MyEnum {\n\tElement;\n\n\tFoo foo;\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, CollectionHelpers.asList(new JavaElement("Element")), CollectionHelpers.asList(new JavaField(new JavaType("Foo"), "foo")))));
		Assert.assertEquals("enum MyEnum {\n\t;\n\n\tFoo foo;\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Enum, "MyEnum", null, null, null, CollectionHelpers.asList(new JavaField(new JavaType("Foo"), "foo")))));
	}

	@Test
	public void testInnerInterface() {
		Assert.assertEquals("class MyClass {\n\tstatic interface MyInterface {}\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, "MyClass", null, null, null, CollectionHelpers.asList(new JavaTypeDeclaration(null, JavaProtection.Unspecified, EnumSet.of(JavaModifier.Static), JavaMetaType.Interface, "MyInterface", null, null, null, null)))));
	}
	
	@Test
	public void testParents() {
		Assert.assertEquals("public class MyClass extends String {}", renderString.render(new JavaTypeDeclaration(JavaTypeDeclaration.STANDARD, "MyClass").setSuperclass(new JavaType(String.class))));
		Assert.assertEquals("public class MyClass extends String implements ISomething {}", renderString.render(new JavaTypeDeclaration(JavaTypeDeclaration.STANDARD, "MyClass").setSuperclass(new JavaType(String.class)).setInterfaces(CollectionHelpers.asList(new JavaType("ISomething")))));
		Assert.assertEquals("public class MyClass implements ISomething {}", renderString.render(new JavaTypeDeclaration(JavaTypeDeclaration.STANDARD, "MyClass").setInterfaces(CollectionHelpers.asList(new JavaType("ISomething")))));
		Assert.assertEquals("public interface MyInterface extends ISomething0, ISomething1 {}", renderString.render(new JavaTypeDeclaration(JavaTypeDeclaration.STANDARD, "MyInterface").setMeta(JavaMetaType.Interface).setInterfaces(CollectionHelpers.asList(new JavaType("ISomething0"), new JavaType("ISomething1")))));
	}

	@Test
	public void testFile() {
		final JavaType string = new JavaType(String.class);
		final JavaMethod method = new JavaMethod(string, "toString").setStatements(CollectionHelpers.asList(new JavaVariable(JavaVariable.STANDARD, string, "retVal").setAnnotations(CollectionHelpers.asList(new JavaAnnotation(string)))));
		final String actual = renderFile.render(new JavaFile(new JavaPackageSpecifier("foo"), CollectionHelpers.asList(new JavaImport(new JavaType("foo.Other"))), CollectionHelpers.asList(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Class, "Test", null, null, null, CollectionHelpers.asList(method)))));

		final String expected = ResourceHelpers.read(getClass(), "Test.java.txt");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMethod() {
		final JavaType string = new JavaType(String.class);
		final List<JavaAnnotation> annotations = CollectionHelpers.asList(new JavaAnnotation(string));
		Assert.assertEquals("@String\nclass MyClass {\n\t@String\n\tpublic String toString() {}\n}", renderString.render(new JavaTypeDeclaration(annotations, JavaProtection.Unspecified, null, JavaMetaType.Class, "MyClass", null, null, null, CollectionHelpers.asList(new JavaMethod(string, "toString").setAnnotations(annotations).setStatements(CollectionHelpers.getEmpty())))));
		Assert.assertEquals("interface MyInterface {\n\tpublic String toString();\n}", renderString.render(new JavaTypeDeclaration(null, JavaProtection.Unspecified, null, JavaMetaType.Interface, "MyInterface", null, null, null, CollectionHelpers.asList(new JavaMethod(string, "toString")))));
	}

	@Test
	public void testVariable() {
		final JavaVariable variable = new JavaVariable(new JavaType("Boolean"), "var").setInitializer(JavaNull.singleton);
		Assert.assertEquals("final Boolean var = null;", renderString.render(variable));
	}
}
