package com.g2forge.enigma.javagen.type.decl;

import java.util.Collection;
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
public class JavaField implements IJavaMember, IJavaVariable {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection><modifiers:{modifier|<modifier> }><type> <name><if(initializer)> = <initializer><endif>;";

	public static final JavaField STANDARD = new JavaField(null, JavaProtection.Unspecified, null, null, null, null);

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;

	protected Set<JavaModifier> modifiers;

	protected JavaType type;

	protected String name;

	protected IJavaExpression initializer;

	public JavaField(JavaType type, String name) {
		this(STANDARD, type, name);
	}

	public JavaField(JavaField base, JavaType type, String name) {
		this(base.getAnnotations(), base.getProtection(), base.getModifiers(), type, name, base.getInitializer());
	}
}