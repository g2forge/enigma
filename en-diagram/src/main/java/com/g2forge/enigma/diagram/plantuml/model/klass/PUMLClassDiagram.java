package com.g2forge.enigma.diagram.plantuml.model.klass;

import java.util.List;

import com.g2forge.enigma.diagram.plantuml.model.IPUMLDiagram;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLClassDiagram implements IPUMLDiagram {
	@Singular
	protected final List<PUMLClass> uclasses;

	@Singular
	protected final List<PUMLRelation> relations;
}
