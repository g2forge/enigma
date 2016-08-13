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
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection><type> <name>(){<if(statements)>\n\t<statements;separator=\"\\n\">\n<endif>}";

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;

	protected JavaType type;

	protected String name;

	protected Collection<IJavaStatement> statements;
}
