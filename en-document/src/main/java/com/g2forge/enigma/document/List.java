package com.g2forge.enigma.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class List implements IBlock {
	@Getter
	@AllArgsConstructor
	public enum Marker {
		Ordered,
		Numbered;
	}

	protected final Marker marker;

	@Singular
	protected final java.util.List<IListItem> items;
}
