package com.g2forge.enigma.presentation.layout;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xslf.usermodel.XSLFSimpleShape;

import com.g2forge.alexandria.java.core.iface.ISingleton;
import com.g2forge.enigma.presentation.content.IContent;

public class UniformLayoutSlide implements ILayoutSlide, ISingleton {
	protected double scale = Double.MAX_VALUE;

	protected final List<Runnable> applications = new ArrayList<>();

	@Override
	public void close() {
		applications.forEach(Runnable::run);
	}

	@Override
	public ILayoutContent getContent(IContent content, XSLFSimpleShape target) {
		return new ILayoutContent() {
			@Override
			public void anchor(Rectangle2D anchor, Dimension content, boolean lockAspectRatio) {
				if (!lockAspectRatio) target.setAnchor(ILayoutContent.computeCenterAndScale(anchor, content, lockAspectRatio));
				UniformLayoutSlide.this.scale = Math.min(UniformLayoutSlide.this.scale, ILayoutContent.scale(anchor, content));
				applications.add(() -> target.setAnchor(ILayoutContent.center(ILayoutContent.scale(content, scale), anchor)));
			}
		};
	}
}
