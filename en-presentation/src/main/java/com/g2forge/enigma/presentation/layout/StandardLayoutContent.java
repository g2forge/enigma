package com.g2forge.enigma.presentation.layout;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import org.apache.poi.xslf.usermodel.XSLFSimpleShape;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StandardLayoutContent implements ILayoutContent {
	protected final XSLFSimpleShape target;

	public void anchor(Rectangle2D anchor, Dimension content, boolean lockAspectRatio) {
		target.setAnchor(ILayoutContent.computeCenterAndScale(anchor, content, lockAspectRatio));
	}
}