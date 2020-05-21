package com.g2forge.enigma.web.css.model.distance;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Distance implements IDistance {
	public enum Unit {
		PX;
	}

	protected final int amount;

	protected final Unit unit;
}
