package com.g2forge.enigma.presentation.text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Font {
	protected final String family;

	protected final double size;
}
