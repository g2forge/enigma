package com.g2forge.enigma.diagram.plantuml.model.component;

import com.g2forge.alexandria.java.adt.name.IStringNamed;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLComponent implements IStringNamed {
	protected final String name;
}
