package com.g2forge.enigma.javagen.core;

import java.util.Collection;

public interface IJavaAnnotated {
	public static final String TEMPLATE_ANNOTATIONS = "<if(annotations)><annotations;separator=\"\\n\">\n<endif>";

	public Collection<JavaAnnotation> getAnnotations();
}
