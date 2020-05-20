package com.g2forge.enigma.diagram.plantuml.model.sequence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PUMLMessage implements IPUMLEvent {
	protected final String source;

	protected final String destination;

	protected final String label;
}
