package com.g2forge.enigma.presentation;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.g2forge.enigma.presentation.content.IContent;
import com.g2forge.enigma.presentation.layout.ILayoutSlide;

public interface ISlideContext {
	public void addContent(int placeholder, final IContent content);

	public ILayoutSlide getLayout();

	public XMLSlideShow getShow();
	
	public XSLFSlide getSlide();
}
