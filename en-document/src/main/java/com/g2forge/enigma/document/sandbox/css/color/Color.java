package com.g2forge.enigma.document.sandbox.css.color;

import com.g2forge.alexandria.java.core.helpers.HValidate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Color implements IColor {
	protected final int r;

	protected final int g;

	protected final int b;

	public Color(int r, int g, int b) {
		this.r = HValidate.inRange(r, 0, 256);
		this.g = HValidate.inRange(g, 0, 256);
		this.b = HValidate.inRange(b, 0, 256);
	}
}
