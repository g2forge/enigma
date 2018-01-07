package com.g2forge.enigma.presentation.slide;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class SlideSection implements ISlide {
	protected final String title;

	protected final String subtitle;

	@Singular
	protected final List<ISlide> slides;
}
