package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JavaClass extends AJavaDeclaration {
	protected Collection<AJavaMember> members;

	public JavaClass(Collection<JavaAnnotation> annotations, JavaProtection protection, String name, Collection<AJavaMember> fields) {
		super(annotations, protection, name);
		this.members = fields;
	}
}
