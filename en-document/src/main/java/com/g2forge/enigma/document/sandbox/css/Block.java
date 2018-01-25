package com.g2forge.enigma.document.sandbox.css;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Block implements ICSSStyle {
	@Singular
	protected final List<ICSSStyle> styles;

	public Block(ICSSStyle... styles) {
		this(Arrays.asList(styles));
	}
}
