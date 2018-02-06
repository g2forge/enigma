package com.g2forge.enigma.document.convert.md;

import java.lang.reflect.Type;

public interface IMDRenderContext {
	public StringBuilder getBuilder();

	public IExplicitMDElement toExplicit(Object object, Type type);

	public String getNewline();
}
