package com.g2forge.enigma.diagram.plantuml.model.klass;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLSpotStereotype implements IPUMLStereotype {
	public static final String C_COLOR = "#ADD1B2";

	protected final char letter;

	protected final String color;
}
