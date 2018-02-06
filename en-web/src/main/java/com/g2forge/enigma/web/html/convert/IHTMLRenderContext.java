package com.g2forge.enigma.web.html.convert;

import java.lang.reflect.Type;

public interface IHTMLRenderContext {
	public StringBuilder getBuilder();

	public IExplicitHTMLElement toExplicit(Object object, Type type);

	public String getNewline();
}
