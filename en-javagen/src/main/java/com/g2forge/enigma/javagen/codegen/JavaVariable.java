package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JavaVariable extends AJavaAnnotated {
	protected boolean isFinal;

	protected JavaType type;

	protected String name;

	public JavaVariable(Collection<JavaAnnotation> annotations, boolean isFinal, JavaType type, String name) {
		super(annotations);
		this.isFinal = isFinal;
		this.type = type;
		this.name = name;
	}
}
