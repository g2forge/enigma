package com.g2forge.enigma.javagen.core;

import java.util.Arrays;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaPackageSpecifier {
	protected static final String TEMPLATE = "<name;separator=\".\">";

	protected Collection<String> name;

	public JavaPackageSpecifier(String... name) {
		this(Arrays.asList(name));
	}
}
