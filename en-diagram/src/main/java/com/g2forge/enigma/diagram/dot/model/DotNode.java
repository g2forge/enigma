package com.g2forge.enigma.diagram.dot.model;

import java.util.List;

import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.enigma.diagram.dot.convert.DotAttribute;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class DotNode implements IDotStatement, IDotAttributed {
	public static class DotNodeBuilder implements IBuilder<DotNode>, IDotAttributed.IDotAttributedBuilder<DotNodeBuilder> {}

	protected final String name;

	@DotAttribute
	protected final String label;

	@DotAttribute
	protected final DotNodeShape shape;

	@DotAttribute
	protected final String style;

	@Singular
	protected final List<IDotAttribute> attributes;

	public DotNode(String name) {
		this(name, null);
	}

	public DotNode(String name, String label) {
		this(name, label, null, null, null);
	}
}
