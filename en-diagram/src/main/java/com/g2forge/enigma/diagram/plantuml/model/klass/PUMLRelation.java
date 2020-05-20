package com.g2forge.enigma.diagram.plantuml.model.klass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLRelation {
	public enum Direction {
		Left,
		Right
	}

	@AllArgsConstructor
	@Getter
	public enum Type {
		Arrow,
		Extension,
		Composition,
		Aggregation
	}

	protected final String left;

	protected final String leftLabel;

	protected final Type type;

	protected final String rightLabel;

	protected final String right;

	protected final String label;

	protected final Direction arrow;

	protected final boolean vertical;
	
	protected final boolean back;
}
