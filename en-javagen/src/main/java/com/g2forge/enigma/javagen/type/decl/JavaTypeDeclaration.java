package com.g2forge.enigma.javagen.type.decl;

import java.util.Collection;
import java.util.Set;

import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaTypeDeclaration implements IJavaTypeDeclaration {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection>" + TEMPLATE_MODIFIERS + "<meta> <name><if(superclass)> extends <superclass><endif><if(interfaces)> <if(interface)>extends<else>implements<endif> <interfaces;separator=\", \"><endif> {<if(enum)><if(elements)>\n\t<elements;separator=\",\\n\\t\"><endif><if(members)><if(elements)><else><\\n><\\t><endif>;<\\n><else><if(elements)><\\n><endif><endif><endif><if(members)>\n\t<members;separator=\"\\n\\n\"><\\n><endif>}";

	public static final JavaTypeDeclaration STANDARD = new JavaTypeDeclaration(null, JavaProtection.Public, null, JavaMetaType.Class, null, null, null, null, null);

	protected Collection<JavaAnnotation> annotations;

	protected JavaProtection protection;
	
	protected Set<JavaModifier> modifiers;

	protected JavaMetaType meta;

	protected String name;
	
	protected JavaType superclass;
	
	protected Collection<JavaType> interfaces;

	protected Collection<JavaElement> elements;

	protected Collection<IJavaMember> members;

	public JavaTypeDeclaration(String name) {
		this(STANDARD, name);
	}

	public JavaTypeDeclaration(JavaTypeDeclaration base, String name) {
		this(base.getAnnotations(), base.getProtection(), base.getModifiers(), base.getMeta(), name, base.getSuperclass(), base.getInterfaces(), base.getElements(), base.getMembers());
	}

	protected boolean isEnum() {
		return JavaMetaType.Enum.equals(getMeta());
	}
	
	protected boolean isInterface() {
		return JavaMetaType.Interface.equals(getMeta());
	}
}
