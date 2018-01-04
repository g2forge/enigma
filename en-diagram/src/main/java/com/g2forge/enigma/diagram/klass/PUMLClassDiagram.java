package com.g2forge.enigma.diagram.klass;

import java.util.List;

import com.g2forge.enigma.diagram.IPUMLDiagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class PUMLClassDiagram implements IPUMLDiagram {
	protected static final String TEMPLATE = "<uclasses:{c|<c><\\n>}><relations:{r|<r><\\n>}>";

	@Singular
	protected final List<PUMLClass> uclasses;

	@Singular
	protected final List<PUMLRelation> relations;
}
