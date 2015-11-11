package com.g2forge.alexandria.java.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleAnnotatedElement implements AnnotatedElement {
	protected final Annotation[] annotations;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		for (Annotation element : annotations) {
			if (annotationClass.isInstance(element)) return (T) element;
		}
		return null;
	}

	@Override
	public Annotation[] getAnnotations() {
		return Arrays.copyOf(annotations, annotations.length);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return getAnnotations();
	}
}
