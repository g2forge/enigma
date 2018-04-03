package com.g2forge.enigma.web.html.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTMLTag {
	public enum Pretty {
		Inline,
		Block,
		NoIndent;
	}

	public String value() default "";

	public Class<? extends IHTMLTagGenerator> generator() default IHTMLTagGenerator.class;

	public Pretty pretty() default Pretty.Block;
}
