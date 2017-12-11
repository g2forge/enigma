package com.g2forge.enigma.javagen.type.decl;

import java.util.Collection;
import java.util.Set;

import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaModifier;
import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class JavaTypeDeclaration implements IJavaTypeDeclaration {
	protected static final String TEMPLATE = TEMPLATE_ANNOTATIONS + "<protection>" + TEMPLATE_MODIFIERS + "<meta> <name><if(superclass)> extends <superclass><endif><if(superinterfaces)> <if(interface)>extends<else>implements<endif> <superinterfaces;separator=\", \"><endif> {<if(enum)><if(elements)>\n\t<elements;separator=\",\\n\\t\"><endif><if(members)><if(elements)><else><\\n><\\t><endif>;<\\n><else><if(elements)><\\n><endif><endif><endif><if(members)>\n\t<members;separator=\"\\n\\n\"><\\n><endif>}";

	public static JavaTypeDeclarationBuilder standardBuilder() {
		return builder().protection(JavaProtection.Public).meta(JavaMetaType.Class);
	}

	@Singular
	protected final Collection<JavaAnnotation> annotations;

	protected final JavaProtection protection;

	@Singular
	protected final Set<JavaModifier> modifiers;

	protected final JavaMetaType meta;

	protected final String name;

	protected final JavaType superclass;

	@Singular
	protected final Collection<JavaType> superinterfaces;

	@Singular
	protected final Collection<JavaElement> elements;

	@Singular
	protected final Collection<IJavaMember> members;

	protected boolean isEnum() {
		return JavaMetaType.Enum.equals(getMeta());
	}

	protected boolean isInterface() {
		return JavaMetaType.Interface.equals(getMeta());
	}
}
