package com.g2forge.enigma.javagen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Property {
	protected Type type;

	protected String name;
}
