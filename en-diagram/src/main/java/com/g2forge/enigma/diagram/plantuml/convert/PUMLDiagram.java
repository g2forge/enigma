package com.g2forge.enigma.diagram.plantuml.convert;

import java.lang.reflect.Type;
import java.util.Collection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
class PUMLDiagram<E, R> {
	@Singular
	protected final Collection<? extends E> entities;
	
	@Singular
	protected final Collection<? extends R> relationships;
	
	protected final Type entityType;
	
	protected final Type relationshipType;
}