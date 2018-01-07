package com.g2forge.enigma.diagram.sequence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PUMLMessage implements IPUMLEvent {
	protected static final String TEMPLATE = "<source> -> <destination><if(label)> : <label><endif>";

	protected final String source;

	protected final String destination;

	protected final String label;
}
