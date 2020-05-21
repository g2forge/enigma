package com.g2forge.enigma.diagram.plantuml.model;

import com.g2forge.enigma.diagram.plantuml.model.style.PUMLControl;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLContent {
	protected final PUMLControl control;

	protected final IPUMLDiagram diagram;
}
