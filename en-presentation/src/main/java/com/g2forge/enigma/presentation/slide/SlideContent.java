package com.g2forge.enigma.presentation.slide;

import com.g2forge.enigma.presentation.content.IContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SlideContent implements ISlide {
	protected final String title;

	protected final String subtitle;

	protected final IContent content;
}
