package com.g2forge.enigma.presentation.content;

import java.awt.geom.Rectangle2D;

import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.g2forge.enigma.presentation.slide.ISlideContext;

public interface IContentContext {
	public Rectangle2D getAnchor();

	public ISlideContext getSlide();

	public XSLFTextShape getText();
}
