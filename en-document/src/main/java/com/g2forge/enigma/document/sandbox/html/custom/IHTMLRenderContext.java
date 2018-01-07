package com.g2forge.enigma.document.sandbox.html.custom;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.function.IFunction2;

public interface IHTMLRenderContext {
	public StringBuilder getBuilder();

	public IFunction2<? super Object, ? super Type, ? extends IExplicitHTMLElement> getExplicit();

	public String getNewline();
}
