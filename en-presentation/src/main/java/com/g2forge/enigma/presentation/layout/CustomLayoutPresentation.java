package com.g2forge.enigma.presentation.layout;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.g2forge.enigma.presentation.slide.ISlide;

public class CustomLayoutPresentation implements ILayoutPresentation {
	protected final Map<ISlide, Supplier<ILayoutSlide>> map = new IdentityHashMap<>();

	@Override
	public ILayoutSlide getSlide(ISlide slide) {
		if (map.containsKey(slide)) return map.get(slide).get();
		return StandardLayoutSlide.create();
	}

	@Override
	public void layout() {}

	public ISlide layout(ISlide slide, ILayoutSlide layout) {
		map.put(slide, () -> layout);
		return slide;
	}
}
