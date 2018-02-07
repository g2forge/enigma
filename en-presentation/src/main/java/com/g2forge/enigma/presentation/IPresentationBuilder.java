package com.g2forge.enigma.presentation;

import java.nio.file.Path;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.presentation.slide.ISlide;

public interface IPresentationBuilder extends ICloseable {
	public IPresentationBuilder add(ISlide slide);

	public IPresentationBuilder write(Path path);
}
