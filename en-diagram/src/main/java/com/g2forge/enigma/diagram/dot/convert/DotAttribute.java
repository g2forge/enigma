package com.g2forge.enigma.diagram.dot.convert;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface DotAttribute {
	public boolean skipNull() default true;

	/**
	 * Get the name of the attribute.
	 * 
	 * @return The name of the attribute.
	 */
	public String value() default "";
}
