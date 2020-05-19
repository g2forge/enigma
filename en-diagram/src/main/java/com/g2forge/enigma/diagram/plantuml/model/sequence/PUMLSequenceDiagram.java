package com.g2forge.enigma.diagram.plantuml.model.sequence;

import java.util.List;

import com.g2forge.enigma.diagram.plantuml.model.IPUMLDiagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class PUMLSequenceDiagram implements IPUMLDiagram {
	protected static final String TEMPLATE = "hide footbox<\\n><participants:{p|<p><\\n>}><events:{e|<e><\\n>}>";

	@Singular
	protected final List<PUMLParticipant> participants;

	@Singular
	protected final List<IPUMLEvent> events;
}
