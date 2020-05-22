package com.g2forge.enigma.document.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class DocList implements IBlock {
	@Getter
	@AllArgsConstructor
	public enum Marker {
		Ordered,
		Numbered;
	}

	protected final Marker marker;

	@Singular
	protected final List<IDocListItem> items;
}
