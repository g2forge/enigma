package com.g2forge.enigma.document.sandbox.html.custom;

import java.lang.reflect.Type;

public interface IHTMLRenderContext {
	public StringBuilder getBuilder();

	public IExplicitHTMLElement toExplicit(Object object, Type type);

	public String getNewline();
}
