package com.g2forge.enigma.diagram.dot.model;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class DotVertex implements IDotStatement, IDotAttributed {
	public static class DotVertexBuilder implements IBuilder<DotVertex>, IDotAttributed.IDotAttributedBuilder<DotVertexBuilder> {}
	
	protected final String name;
	
	@Singular
	protected final List<IDotAttribute> attributes;

	public DotVertex(String name, IDotAttribute...attributes) {
		this(name, HCollection.asList(attributes));
	}
}
