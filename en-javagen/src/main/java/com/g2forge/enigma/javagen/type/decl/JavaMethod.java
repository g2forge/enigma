package com.g2forge.enigma.javagen.type.decl;

import java.util.Collection;
import java.util.Set;

import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.imperative.statement.IJavaStatement;
import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaMethod implements IJavaMember {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection>" + TEMPLATE_MODIFIERS + "<type> <name>()<if(body)> {<if(statements)>\n\t<statements;separator=\"\\n\">\n<endif>}<else>;<endif>";

	public static final JavaMethod STANDARD = new JavaMethod(null, JavaProtection.Public, null, null, null, null);

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;

	protected Set<JavaModifier> modifiers;

	protected JavaType type;

	protected String name;

	protected Collection<IJavaStatement> statements;

	public JavaMethod(JavaMethod base, JavaType type, String name) {
		this(base.getAnnotations(), base.getProtection(), base.getModifiers(), type, name, base.getStatements());
	}

	public JavaMethod(JavaType type, String name) {
		this(STANDARD, type, name);
	}
	
	protected boolean isBody() {
		return getStatements() != null;
	}
}
