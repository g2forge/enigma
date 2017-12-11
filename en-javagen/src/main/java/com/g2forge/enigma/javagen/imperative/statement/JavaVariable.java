package com.g2forge.enigma.javagen.imperative.statement;

import java.util.Collection;
import java.util.Set;

import com.g2forge.enigma.javagen.core.IJavaVariable;
import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.imperative.expression.IJavaExpression;
import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class JavaVariable implements IJavaVariable {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<modifiers:{modifier|<modifier> }><type> <name><if(initializer)> = <initializer><endif>;";

	public static JavaVariableBuilder standardBuilder() {
		return builder().modifier(JavaModifier.Final);
	}

	@Singular
	protected final Collection<JavaAnnotation> annotations;

	@Singular
	protected final Set<JavaModifier> modifiers;

	protected final JavaType type;

	protected final String name;

	protected final IJavaExpression initializer;
}
