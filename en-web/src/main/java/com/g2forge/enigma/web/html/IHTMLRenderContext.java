package com.g2forge.enigma.web.html;

import java.lang.reflect.Type;

public interface IHTMLRenderContext {
	public StringBuilder getBuilder();

	public IExplicitHTMLElement toExplicit(Object object, Type type);

	public String getNewline();
}
