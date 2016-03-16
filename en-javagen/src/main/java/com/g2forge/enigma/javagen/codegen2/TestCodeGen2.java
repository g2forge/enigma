package com.g2forge.enigma.javagen.codegen2;

import java.util.Arrays;

import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

public class TestCodeGen2 {
	public static void main(String[] args) {
		final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer();
		System.out.println(renderer.render(new JavaClass("MyClass", Arrays.asList(new JavaField("String", "foo"), new JavaField("int", "bar")))));
	}
}
