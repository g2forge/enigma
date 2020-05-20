package com.g2forge.enigma.diagram.plantuml.model.style;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class StringPUMLColor implements IPUMLColor {
	protected final String color;
}
