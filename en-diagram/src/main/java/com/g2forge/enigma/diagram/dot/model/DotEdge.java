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
public class DotEdge implements IDotStatement, IDotAttributed {
	public static class DotEdgeBuilder implements IBuilder<DotEdge>, IDotAttributed.IDotAttributedBuilder<DotEdgeBuilder> {}

	@Singular
	protected final List<String> nodes;

	@DotAttribute
	protected final String label;

	@DotAttribute("dir")
	protected final DotEdgeDirection direction;

	@DotAttribute
	protected final String style;

	@Singular
	protected final List<IDotAttribute> attributes;
}
