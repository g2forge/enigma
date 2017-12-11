package com.g2forge.enigma.javagen.imperative.statement;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.g2forge.enigma.javagen.core.IJavaVariable;
import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.imperative.expression.IJavaExpression;
import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaVariable implements IJavaVariable {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<modifiers:{modifier|<modifier> }><type> <name><if(initializer)> = <initializer><endif>;";

	public static final JavaVariable STANDARD = new JavaVariable(null, Collections.unmodifiableSet(EnumSet.of(JavaModifier.Final)), null, null, null);

	protected Collection<JavaAnnotation> annotations;

	protected Set<JavaModifier> modifiers;

	protected JavaType type;

	protected String name;

	protected IJavaExpression initializer;

	public JavaVariable(JavaVariable base, JavaType type, String name) {
		this(base.getAnnotations(), base.getModifiers(), type, name, base.getInitializer());
	}

	public JavaVariable(JavaType type, String name) {
		this(STANDARD, type, name);
	}
}
