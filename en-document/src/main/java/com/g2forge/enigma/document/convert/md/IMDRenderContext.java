package com.g2forge.enigma.document.convert.md;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.close.ICloseable;

public interface IMDRenderContext {
	public StringBuilder getBuilder();

	public String getNewline();

	public int getSectionLevel();

	public ICloseable openSection();

	public IExplicitMDElement toExplicit(Object object, Type type);
}
