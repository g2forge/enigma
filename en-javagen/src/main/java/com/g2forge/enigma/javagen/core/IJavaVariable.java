package com.g2forge.enigma.javagen.core;

import java.util.Set;

import com.g2forge.enigma.javagen.imperative.expression.IJavaExpression;
import com.g2forge.enigma.javagen.imperative.statement.IJavaStatement;
import com.g2forge.enigma.javagen.type.expression.JavaType;

public interface IJavaVariable extends IJavaAnnotated, IJavaStatement, IJavaNamed {
	public Set<JavaModifier> getModifiers();

	public JavaType getType();

	public IJavaExpression getInitializer();
}
