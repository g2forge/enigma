package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AJavaMember extends AJavaDeclaration {
	protected JavaType type;

	public AJavaMember(Collection<JavaAnnotation> annotations, JavaProtection protection, JavaType type, String name) {
		super(annotations, protection, name);
		this.type = type;
	}
}
