package com.g2forge.enigma.web.css.convert;

import java.lang.reflect.Type;

public interface ICSSRenderContext {
	public StringBuilder getBuilder();

	public String getNewline();

	public IExplicitCSSRenderable toExplicit(Object object, Type type);
}
