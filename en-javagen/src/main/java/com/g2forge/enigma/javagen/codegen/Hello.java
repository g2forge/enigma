package com.g2forge.enigma.javagen.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

public class Hello {
	public static void main(String[] args) {
		final JavaRenderer renderer = new JavaRenderer();
		final Path root = Paths.get(System.getProperty("user.dir"), "src/main/java");

		renderer.render(root, new JavaFile("foo.bar", null, new JavaClass(Arrays.asList(new JavaAnnotation(new JavaType(Data.class.getName()))), JavaProtection.Public, "HelloWorld", null)));

		final List<AJavaMember> members = new ArrayList<>();
		final JavaType string = new JavaType(String.class), integer = new JavaType("int");
		members.add(new JavaField(null, JavaProtection.Protected, string, "foo"));
		members.add(new JavaMethod(null, JavaProtection.Protected, string, "getFoo", Arrays.asList(new JavaVariable(null, true, integer, "bar")), Arrays.asList(new JavaAssignment(new JavaReference("foo"), new JavaReference("bar")))));
		members.add(new JavaMethod(null, JavaProtection.Protected, string, "getBar", Arrays.asList(new JavaVariable(null, true, integer, "foo"), new JavaVariable(null, false, string, "juan")), null));
		renderer.render(root, new JavaFile("apackage", null, new JavaClass(null, JavaProtection.PackageProtected, "HelloWorld", members)));
	}
}
