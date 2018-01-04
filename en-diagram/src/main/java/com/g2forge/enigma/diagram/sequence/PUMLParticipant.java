package com.g2forge.enigma.diagram.sequence;

import com.g2forge.alexandria.java.name.IStringNamed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PUMLParticipant implements IStringNamed {
	protected static final String TEMPLATE = "participant <name>";

	protected final String name;
}
