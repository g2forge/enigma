package com.g2forge.enigma.javagen.codegen2;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaMethod implements IJavaMember {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection><type> <name>(){<if(statements)>\n\t<statements;separator=\"\\n\">\n<endif>}";

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;

	protected JavaType type;

	protected String name;

	protected Collection<IJavaStatement> statements;
}
