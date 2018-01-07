package com.g2forge.enigma.presentation.layout;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

public interface ILayoutContent {
	public static Rectangle2D center(Dimension dimension, Rectangle2D center) {
		final double x = center.getCenterX() - (dimension.getWidth() / 2);
		final double y = center.getCenterY() - (dimension.getHeight() / 2);
		return new Rectangle2D.Double(x, y, dimension.getWidth(), dimension.getHeight());
	}

	/**
	 * 
	 * @param anchor
	 *            The placeholder anchor
	 * @param content
	 *            The dimensions of the content
	 * @param lockAspectRatio
	 *            <code>true</code> to preserve the aspect ratio of the content
	 * @return
	 */
	public static Rectangle2D computeCenterAndScale(Rectangle2D anchor, Dimension content, boolean lockAspectRatio) {
		final Dimension scaled;
		if (lockAspectRatio) scaled = scale(content, scale(anchor, content));
		else scaled = toDimension(anchor);
		return center(scaled, anchor);
	}

	public static Dimension scale(Dimension dimension, final double scale) {
		return new Dimension((int) Math.ceil(dimension.getWidth() * scale), (int) Math.ceil(dimension.getHeight() * scale));
	}

	public static double scale(Rectangle2D rectangle, Dimension dimension) {
		if (dimension.getWidth() > dimension.getHeight()) return rectangle.getWidth() / dimension.getWidth();
		else return rectangle.getHeight() / dimension.getHeight();
	}

	public static Dimension toDimension(Rectangle2D rectangle) {
		return new Dimension((int) Math.ceil(rectangle.getWidth()), (int) Math.ceil(rectangle.getHeight()));
	}

	public void anchor(Rectangle2D anchor, Dimension content, boolean lockAspectRatio);
}