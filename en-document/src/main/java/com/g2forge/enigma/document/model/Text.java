package com.g2forge.enigma.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Text implements ISpan {
	protected final String text;
}
