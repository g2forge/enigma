package com.g2forge.enigma.diagram.component;

import java.util.List;

import com.g2forge.enigma.diagram.IPUMLDiagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class PUMLComponentDiagram implements IPUMLDiagram {
	protected static final String TEMPLATE = "<components:{c|<c><\\n>}><links:{l|<l><\\n>}>";

	@Singular
	protected final List<PUMLComponent> components;

	@Singular
	protected final List<PUMLLink> links;
}
