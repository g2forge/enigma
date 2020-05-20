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
	@Singular
	protected final List<PUMLParticipant> participants;

	@Singular
	protected final List<IPUMLEvent> events;
}
