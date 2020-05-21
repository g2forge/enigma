package com.g2forge.enigma.diagram.plantuml.model.sequence;

import com.g2forge.alexandria.java.adt.name.IStringNamed;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLParticipant implements IStringNamed {
	protected final String name;
}
