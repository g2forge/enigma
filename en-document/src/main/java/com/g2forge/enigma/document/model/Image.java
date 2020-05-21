package com.g2forge.enigma.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Image implements IBlock {
	protected final String alt;

	protected final String url;
}
