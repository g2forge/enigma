package com.g2forge.enigma.diagram.component;

import com.g2forge.alexandria.java.name.IStringNamed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PUMLComponent implements IStringNamed {
	protected static final String TEMPLATE = "[<name>]<if(mangled)><mangled><endif>";

	protected static String mangle(final String name) {
		return name.replaceAll("[^a-zA-Z0-9_]", "_");
	}

	protected final String name;

	protected String getMangled() {
		final String name = getName();
		if (name.matches("[a-zA-Z_][a-zA-Z0-9_]*")) return null;
		return mangle(name);
	}
}
