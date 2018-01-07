package com.g2forge.enigma.presentation.content;

import com.g2forge.enigma.presentation.text.Font;
import com.g2forge.enigma.presentation.text.Paragraph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContentText implements IContent {
	protected final Paragraph paragraph;

	protected final Font font;

	protected final String text;
}
