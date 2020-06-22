package com.g2forge.enigma.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Definition implements IDocListItem {
	protected final ISpan term;
	
	protected final IBlock body;
}
