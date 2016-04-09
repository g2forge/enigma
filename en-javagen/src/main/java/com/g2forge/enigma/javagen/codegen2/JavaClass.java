package com.g2forge.enigma.javagen.codegen2;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaClass implements IJavaAnnotated {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection>class <name> {<if(members)>\n\t<members;separator=\"\\n\\n\">\n<endif>}";

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;

	protected String name;

	protected Collection<IJavaMember> members;
}
