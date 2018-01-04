package com.g2forge.enigma.presentation.layout;

import org.apache.poi.xslf.usermodel.XSLFSimpleShape;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.presentation.content.IContent;

public interface ILayoutSlide extends ICloseable {
	public ILayoutContent getContent(IContent content, XSLFSimpleShape target);
}