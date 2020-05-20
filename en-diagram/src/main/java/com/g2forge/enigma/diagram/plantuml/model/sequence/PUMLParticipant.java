package com.g2forge.enigma.diagram.plantuml.model.sequence;

import com.g2forge.alexandria.java.adt.name.IStringNamed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PUMLParticipant implements IStringNamed {
	protected final String name;
}
