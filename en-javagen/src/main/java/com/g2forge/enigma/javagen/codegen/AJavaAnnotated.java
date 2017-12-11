package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AJavaAnnotated {
	protected Collection<JavaAnnotation> annotations;
}
