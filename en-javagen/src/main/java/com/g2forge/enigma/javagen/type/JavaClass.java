package com.g2forge.enigma.javagen.type;

import java.util.Collection;

import com.g2forge.enigma.javagen.core.JavaAnnotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaClass implements IJavaTypeDeclaration {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection>class <name> {<if(members)>\n\t<members;separator=\"\\n\\n\">\n<endif>}";

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;

	protected String name;

	protected Collection<IJavaMember> members;
}
