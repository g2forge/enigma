package com.g2forge.enigma.presentation.layout;

import org.apache.poi.xslf.usermodel.XSLFSimpleShape;

import com.g2forge.alexandria.java.core.iface.ISingleton;
import com.g2forge.enigma.presentation.content.IContent;

public class StandardLayoutSlide implements ILayoutSlide, ISingleton {
	protected static StandardLayoutSlide INSTANCE = new StandardLayoutSlide();

	public static StandardLayoutSlide create() {
		return INSTANCE;
	}

	@Override
	public void close() {}

	@Override
	public ILayoutContent getContent(IContent content, XSLFSimpleShape target) {
		return new StandardLayoutContent(target);
	}
}
