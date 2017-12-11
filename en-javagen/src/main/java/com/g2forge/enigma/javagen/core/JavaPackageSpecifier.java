package com.g2forge.enigma.javagen.core;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JavaPackageSpecifier {
	protected static final String TEMPLATE = "<name;separator=\".\">";

	protected final Collection<String> name;

	public JavaPackageSpecifier(String... name) {
		this(HCollection.asList(name));
	}
}
