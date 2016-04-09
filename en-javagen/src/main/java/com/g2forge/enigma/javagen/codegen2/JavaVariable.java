package com.g2forge.enigma.javagen.codegen2;

import java.util.Collection;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaVariable implements IJavaAnnotated, IJavaStatement {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<modifiers:{modifier|<modifier> }><type> <name>;";

	protected Collection<JavaAnnotation> annotations;

	protected Set<JavaModifier> modifiers;

	protected JavaType type;

	protected String name;
}
