package com.g2forge.enigma.javagen.type;

import java.util.Collection;

import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.statement.IJavaStatement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaMethod implements IJavaMember {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection><type> <name>() {<if(statements)>\n\t<statements;separator=\"\\n\">\n<endif>}";

	public static final JavaMethod STANDARD = new JavaMethod(null, JavaProtection.Public, null, null, null);

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;

	protected JavaType type;

	protected String name;

	protected Collection<IJavaStatement> statements;

	public JavaMethod(JavaMethod base, JavaType type, String name) {
		this(base.getAnnotations(), base.getProtection(), type, name, base.getStatements());
	}

	public JavaMethod(JavaType type, String name) {
		this(STANDARD, type, name);
	}
}
