package com.g2forge.enigma.diagram.plantuml.model.klass;

import com.g2forge.enigma.diagram.plantuml.model.style.IPUMLColor;
import com.g2forge.enigma.diagram.plantuml.model.style.StringPUMLColor;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class SpotPUMLStereotype implements IPUMLStereotype {
	public static final IPUMLColor C_COLOR = new StringPUMLColor("#ADD1B2");

	protected final char letter;

	protected final IPUMLColor color;
}
