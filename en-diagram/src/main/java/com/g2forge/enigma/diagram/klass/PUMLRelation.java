package com.g2forge.enigma.diagram.klass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class PUMLRelation {
	@AllArgsConstructor
	@Getter
	public enum Direction {
		Left("<"),
		Right(">");

		protected final String plantUML;
	}

	@AllArgsConstructor
	@Getter
	public enum Type {
		Arrow("<--"),
		Extension("<|--"),
		Composition("*--"),
		Aggregation("o--");

		protected final String plantUML;
	}

	protected static final String TEMPLATE = "<left><if(leftLabel)> \"<leftLabel>\"<endif> <type.plantUML> <if(rightLabel)>\"<rightLabel>\" <endif><right><if(label)> : <label><if(arrow)> <arrow.plantUML><endif><endif>";

	protected final PUMLClassName left;

	protected final String leftLabel;

	protected final Type type;

	protected final String rightLabel;

	protected final PUMLClassName right;

	protected final String label;

	protected final Direction arrow;
}
