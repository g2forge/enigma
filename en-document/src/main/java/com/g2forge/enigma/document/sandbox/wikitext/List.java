package com.g2forge.enigma.document.sandbox.wikitext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class List {
	@Getter
	@AllArgsConstructor
	public enum Marker {
		Ordered("*"),
		Numbered("<i>.");

		protected static final String TEMPLATE = "<prefix>";

		protected final String prefix;
	}

	protected static final String TEMPLATE = "<contents:{c|<marker><c><\\n>}>";

	protected final Marker marker;

	@Singular
	protected final java.util.List<ISpan> contents;
}
