package com.g2forge.enigma.presentation.slide;

import org.apache.poi.xslf.usermodel.SlideLayout;

public interface ISlideContextGenerator {
	public ISlideContext create(String layout);

	public ISlideContext create(SlideLayout layout);
}
