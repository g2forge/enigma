package com.g2forge.enigma.javagen.type.decl;

import java.util.Collection;
import java.util.Set;

import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.imperative.statement.JavaBlock;
import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class JavaMethod implements IJavaMember {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection>" + TEMPLATE_MODIFIERS + "<type> <name>()<if(body)> <body><else>;<endif>";

	public static JavaMethodBuilder standardBuilder() {
		return builder().protection(JavaProtection.Public);
	}

	@Singular
	protected final Collection<JavaAnnotation> annotations;

	protected final JavaProtection protection;

	@Singular
	protected final Set<JavaModifier> modifiers;

	protected final JavaType type;

	protected final String name;

	protected final JavaBlock body;
}
