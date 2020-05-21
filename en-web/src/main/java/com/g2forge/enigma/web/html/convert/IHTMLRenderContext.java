package com.g2forge.enigma.web.html.convert;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.close.ICloseable;

public interface IHTMLRenderContext {
	public StringBuilder getBuilder();

	public IExplicitHTMLRenderable toExplicit(Object object, Type type);

	public void newline();

	public ICloseable indent();
}
