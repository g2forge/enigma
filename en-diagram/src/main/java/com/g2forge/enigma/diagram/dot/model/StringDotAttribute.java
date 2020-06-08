package com.g2forge.enigma.diagram.dot.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class StringDotAttribute implements IDotAttribute {
	protected final String name;
	
	protected final String value;
}
