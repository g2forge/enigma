package com.g2forge.enigma.diagram.plantuml.convert;

import java.util.Collection;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
class PUMLRelationship {
	@Getter
	@RequiredArgsConstructor
	protected enum RelationshipArrowStyle {
		Normal("<", ">");

		protected final String back, forward;
	}

	@Getter
	@RequiredArgsConstructor
	protected enum RelationshipLineStyle {
		Dash('-'),
		Dot('.');

		protected final char character;
	}

	public static PUMLRelationshipBuilder builder() {
		return new PUMLRelationshipBuilder();
	}

	public static PUMLRelationshipBuilder builder(String left, String right) {
		return builder().left(left).right(right);
	}

	protected final String left;

	protected final String right;

	@Builder.Default
	protected final RelationshipLineStyle line = RelationshipLineStyle.Dash;

	@Builder.Default
	protected final RelationshipArrowStyle arrow = RelationshipArrowStyle.Normal;

	protected final boolean two;

	protected final boolean back;

	@Singular
	protected final Collection<String> modifiers;

	protected final String label;
}