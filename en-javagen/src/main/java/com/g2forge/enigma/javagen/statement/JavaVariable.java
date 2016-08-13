package com.g2forge.enigma.javagen.statement;

import java.util.Collection;
import java.util.Set;

import com.g2forge.enigma.javagen.core.IJavaAnnotated;
import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.expression.IJavaExpression;
import com.g2forge.enigma.javagen.type.JavaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaVariable implements IJavaAnnotated, IJavaStatement {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<modifiers:{modifier|<modifier> }><type> <name><if(initializer)> = <initializer><endif>;";

	protected Collection<JavaAnnotation> annotations;

	protected Set<JavaModifier> modifiers;

	protected JavaType type;

	protected String name;

	protected IJavaExpression initializer;

	public JavaVariable(JavaType type, String name) {
		this(null, null, type, name, null);
	}
}
