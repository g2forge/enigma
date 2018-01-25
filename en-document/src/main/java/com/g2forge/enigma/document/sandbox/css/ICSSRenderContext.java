package com.g2forge.enigma.document.sandbox.css;

import java.lang.reflect.Type;

public interface ICSSRenderContext {
	public StringBuilder getBuilder();

	public String getNewline();

	public IExplicitCSSRenderable toExplicit(Object object, Type type);
}
