package com.g2forge.enigma.diagram.plantuml.model.sequence;

import java.util.List;

import com.g2forge.enigma.diagram.plantuml.model.IPUMLDiagram;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLSequenceDiagram implements IPUMLDiagram {
	@Builder.Default
	protected final boolean footbox = true;
	
	@Singular
	protected final List<PUMLParticipant> participants;

	@Singular
	protected final List<IPUMLEvent> events;
}
