package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JavaField extends AJavaMember {
	public JavaField(Collection<JavaAnnotation> annotations, JavaProtection protection, JavaType type, String name) {
		super(annotations, protection, type, name);
	}
}
