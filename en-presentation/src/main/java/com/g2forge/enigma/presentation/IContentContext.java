package com.g2forge.enigma.presentation;

import java.awt.geom.Rectangle2D;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.g2forge.enigma.presentation.layout.ILayoutContent;

public interface IContentContext {
	public Rectangle2D getAnchor();

	public ILayoutContent getLayout(XSLFSimpleShape shape);

	public XMLSlideShow getShow();

	public XSLFSlide getSlide();
}
