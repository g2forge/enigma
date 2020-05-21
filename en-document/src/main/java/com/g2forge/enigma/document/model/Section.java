package com.g2forge.enigma.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Section implements IBlock {
	protected final ISpan title;

	protected final IBlock body;
}
