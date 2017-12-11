package com.g2forge.enigma.javagen.core;

import java.util.Set;

public interface IJavaModified {
	public static final String TEMPLATE_MODIFIERS = "<modifiers:{modifier|<modifier> }>";

	public Set<JavaModifier> getModifiers();
}
