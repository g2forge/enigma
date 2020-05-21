package com.g2forge.enigma.diagram.plantuml.model.klass;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class NamedPUMLStereotype implements IPUMLStereotype {
	protected final String name;
}
