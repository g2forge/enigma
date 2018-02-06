package com.g2forge.enigma.web.html.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTMLField {
	public boolean property() default true;

	public boolean skipNull() default true;
}
