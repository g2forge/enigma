package com.g2forge.enigma.diagram.dot.model;

import java.util.List;

import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class DotGraph {
	public static class DotGraphBuilder implements IBuilder<DotGraph> {
		public DotGraphBuilder attribute$(String name, String value) {
			return statement(new StringDotAttribute(name, value));
		}
	}
	
	protected final boolean directed;

	protected final String name;
	
	@Singular
	protected final List<IDotStatement> statements;
}
