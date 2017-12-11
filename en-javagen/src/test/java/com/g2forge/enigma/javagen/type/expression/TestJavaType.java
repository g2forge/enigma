package com.g2forge.enigma.javagen.type.expression;

import java.lang.reflect.Type;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.reflection.typed.ATypeReference;
import com.g2forge.enigma.javagen.ATestJavagen;

public class TestJavaType extends ATestJavagen {
	@Test
	public void testJavaLang() {
		Assert.assertEquals("String", renderString.render(new JavaType(String.class)));
	}

	@Test
	public void testQualified() {
		Assert.assertEquals("com.g2forge.enigma.javagen.type.expression.TestJavaType", renderString.render(new JavaType(getClass())));
	}

	@Test
	public void testGeneric() {
		final ATypeReference<Collection<String>> reference = new ATypeReference<Collection<String>>() {};
		final IJavaTypeReflection<Collection<String>> reflection = reference.getType();
		final IJavaType javaType = reflection.getType().resolve();
		final Type type = javaType.getJavaType();
		Assert.assertEquals("java.util.Collection<java.lang.String>", renderString.render(new JavaType(type)));
	}
}
