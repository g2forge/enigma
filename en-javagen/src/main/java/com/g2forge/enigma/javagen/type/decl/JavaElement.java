package com.g2forge.enigma.javagen.type.decl;

import com.g2forge.enigma.javagen.core.IJavaNamed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaElement implements IJavaNamed {
	protected static final String TEMPLATE = "<name>";

	protected String name;
}
