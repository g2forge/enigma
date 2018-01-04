package com.g2forge.enigma.presentation.layout;

import com.g2forge.enigma.presentation.slide.ISlide;

public interface ILayoutPresentation {
	public ILayoutSlide getSlide(ISlide slide);

	public void layout();
}