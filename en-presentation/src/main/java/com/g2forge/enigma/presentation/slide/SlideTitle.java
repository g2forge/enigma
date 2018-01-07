package com.g2forge.enigma.presentation.slide;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SlideTitle implements ISlide {
	protected final String title;

	protected final String subtitle;
}
