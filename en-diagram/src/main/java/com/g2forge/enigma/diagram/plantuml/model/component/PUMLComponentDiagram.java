package com.g2forge.enigma.diagram.plantuml.model.component;

import java.util.List;

import com.g2forge.enigma.diagram.plantuml.model.IPUMLDiagram;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLComponentDiagram implements IPUMLDiagram {
	@Singular
	protected final List<PUMLComponent> components;

	@Singular
	protected final List<PUMLLink> links;
}
