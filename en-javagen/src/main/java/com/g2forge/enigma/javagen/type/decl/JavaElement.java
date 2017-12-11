package com.g2forge.enigma.javagen.type.decl;

import com.g2forge.enigma.javagen.core.IJavaNamed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JavaElement implements IJavaNamed {
	protected static final String TEMPLATE = "<name>";

	protected final String name;
}
