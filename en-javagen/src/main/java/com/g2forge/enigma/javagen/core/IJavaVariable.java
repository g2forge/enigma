package com.g2forge.enigma.javagen.core;

import java.util.Set;

import com.g2forge.enigma.javagen.expression.IJavaExpression;
import com.g2forge.enigma.javagen.statement.IJavaStatement;
import com.g2forge.enigma.javagen.type.JavaType;

public interface IJavaVariable extends IJavaAnnotated, IJavaStatement, IJavaNamed {
	public Set<JavaModifier> getModifiers();

	public JavaType getType();

	public IJavaExpression getInitializer();
}
