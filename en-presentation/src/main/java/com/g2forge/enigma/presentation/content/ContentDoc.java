package com.g2forge.enigma.presentation.content;

import com.g2forge.enigma.document.convert.IDocElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContentDoc implements IContent {
	protected final IDocElement doc;
}
