package com.g2forge.enigma.presentation.layout;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.enigma.presentation.slide.ISlide;

public class StandardLayoutPresentation implements ILayoutPresentation, ISingleton {
	protected static StandardLayoutPresentation INSTANCE = new StandardLayoutPresentation();

	public static StandardLayoutPresentation create() {
		return INSTANCE;
	}

	@Override
	public ILayoutSlide getSlide(ISlide slide) {
		return StandardLayoutSlide.create();
	}

	@Override
	public void layout() {}
}
