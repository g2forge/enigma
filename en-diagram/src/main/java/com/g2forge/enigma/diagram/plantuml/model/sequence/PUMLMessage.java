package com.g2forge.enigma.diagram.plantuml.model.sequence;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLMessage implements IPUMLEvent {
	protected final String source;

	protected final String destination;

	protected final String label;
}
