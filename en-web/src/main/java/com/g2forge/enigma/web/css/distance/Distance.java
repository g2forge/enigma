package com.g2forge.enigma.web.css.distance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Distance implements IDistance {
	public enum Unit {
		PX;
	}

	protected final int amount;

	protected final Unit unit;
}
