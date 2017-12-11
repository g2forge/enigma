package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AJavaDeclaration extends AJavaAnnotated {
	protected JavaProtection protection;

	protected String name;

	public AJavaDeclaration(Collection<JavaAnnotation> annotations, JavaProtection protection, String name) {
		super(annotations);
		this.protection = protection;
		this.name = name;
	}
}