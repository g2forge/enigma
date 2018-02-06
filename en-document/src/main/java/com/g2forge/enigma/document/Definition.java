package com.g2forge.enigma.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Definition implements IListItem {
	protected final ISpan term;
	
	protected final IBlock body;
}
