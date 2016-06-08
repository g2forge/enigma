package com.g2forge.enigma.javagen;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.CollectionHelpers;
import com.g2forge.enigma.javagen.codegen.AJavaMember;
import com.g2forge.enigma.javagen.codegen.JavaAnnotation;
import com.g2forge.enigma.javagen.codegen.JavaAssignment;
import com.g2forge.enigma.javagen.codegen.JavaClass;
import com.g2forge.enigma.javagen.codegen.JavaField;
import com.g2forge.enigma.javagen.codegen.JavaFile;
import com.g2forge.enigma.javagen.codegen.JavaMethod;
import com.g2forge.enigma.javagen.codegen.JavaProtection;
import com.g2forge.enigma.javagen.codegen.JavaReference;
import com.g2forge.enigma.javagen.codegen.JavaRenderer;
import com.g2forge.enigma.javagen.codegen.JavaType;
import com.g2forge.enigma.javagen.codegen.JavaVariable;

import lombok.Data;

public class CodeGenExample {
	public static void main(String[] args) {
		final JavaRenderer renderer = new JavaRenderer();
		final Path root = Paths.get(System.getProperty("user.dir"), "src/main/java");

		renderer.render(root, new JavaFile("foo.bar", null, new JavaClass(CollectionHelpers.asList(new JavaAnnotation(new JavaType(Data.class.getName()))), JavaProtection.Public, "HelloWorld", null)));

		final List<AJavaMember> members = new ArrayList<>();
		final JavaType string = new JavaType(String.class), integer = new JavaType("int");
		members.add(new JavaField(null, JavaProtection.Protected, string, "foo"));
		members.add(new JavaMethod(null, JavaProtection.Protected, string, "getFoo", CollectionHelpers.asList(new JavaVariable(null, true, integer, "bar")), CollectionHelpers.asList(new JavaAssignment(new JavaReference("foo"), new JavaReference("bar")))));
		members.add(new JavaMethod(null, JavaProtection.Protected, string, "getBar", CollectionHelpers.asList(new JavaVariable(null, true, integer, "foo"), new JavaVariable(null, false, string, "juan")), null));
		renderer.render(root, new JavaFile("apackage", null, new JavaClass(null, JavaProtection.PackageProtected, "HelloWorld", members)));
	}
}
