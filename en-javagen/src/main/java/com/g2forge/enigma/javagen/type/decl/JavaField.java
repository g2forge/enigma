package com.g2forge.enigma.javagen.type.decl;

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

@Data
@Builder
@AllArgsConstructor
public class JavaField implements IJavaMember, IJavaVariable {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection><modifiers:{modifier|<modifier> }><type> <name><if(initializer)> = <initializer><endif>;";

	public static JavaFieldBuilder standardBuilder() {
		return builder().protection(JavaProtection.Unspecified);
	}

	protected final Collection<JavaAnnotation> annotations;

	protected final JavaProtection protection;

	protected final Set<JavaModifier> modifiers;

	protected final JavaType type;

	protected final String name;

	protected final IJavaExpression initializer;
}
