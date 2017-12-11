package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JavaMethod extends AJavaMember {
	protected Collection<JavaVariable> arguments;

	protected Collection<IJavaStatement> statements;

	public JavaMethod(Collection<JavaAnnotation> annotations, JavaProtection protection, JavaType type, String name, Collection<JavaVariable> arguments, Collection<IJavaStatement> statements) {
		super(annotations, protection, type, name);
		this.arguments = arguments;
		this.statements = statements;
	}
}
