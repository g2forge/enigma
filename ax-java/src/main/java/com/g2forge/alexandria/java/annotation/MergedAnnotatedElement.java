package com.g2forge.alexandria.java.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MergedAnnotatedElement implements AnnotatedElement {
	protected final Collection<AnnotatedElement> elements;

	protected Annotation[] flatten(Function<AnnotatedElement, Annotation[]> function) {
		return elements.stream().flatMap(function.andThen(Stream::of)).toArray(size -> new Annotation[size]);
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		for (AnnotatedElement element : elements) {
			final T retVal = element.getAnnotation(annotationClass);
			if (retVal != null) return retVal;
		}
		return null;
	}

	@Override
	public Annotation[] getAnnotations() {
		return flatten(AnnotatedElement::getAnnotations);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return flatten(AnnotatedElement::getDeclaredAnnotations);
	}
}
