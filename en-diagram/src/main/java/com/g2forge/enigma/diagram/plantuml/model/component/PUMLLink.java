package com.g2forge.enigma.diagram.plantuml.model.component;

import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLLink {
	public enum Modifier {
		Hidden;
	}

	public static PUMLLinkBuilder builder() {
		return new PUMLLinkBuilder();
	}

	public static PUMLLinkBuilder builder(String left, String right) {
		return builder().left(left).right(right);
	}

	protected final String left;

	protected final boolean vertical;

	@Singular
	protected final Set<Modifier> modifiers;

	protected final String right;

	public PUMLLink(String left, String right) {
		this(left, false, null, right);
	}
}
