package com.g2forge.enigma.document.sandbox.wikitext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Text implements ISpan {
	protected static final String TEMPLATE = "<text>";

	protected final String text;
}
