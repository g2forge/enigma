package com.g2forge.enigma.javagen.codegen2;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaClass {
	protected static final String TEMPLATE = "<protection;format=\"java\">class <name> {<if(members)>\n\t<members:{member | <member;format=\"java\">};separator=\"\\n\\n\">\n<endif>}";

	protected JavaProtection protection;

	protected String name;

	protected Collection<JavaField> members;
}
