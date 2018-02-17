package com.g2forge.enigma.document.convert.md;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.document.convert.md.MDRenderer.LineBreakStrategy;

public interface IMDRenderContext {
	public StringBuilder getBuilder();

	public LineBreakStrategy getLineBreakStrategy();

	public String getNewline();

	public int getSectionLevel();

	public ICloseable openLineBreakStrategy(LineBreakStrategy strategy);

	public ICloseable openSection();

	public IExplicitMDElement toExplicit(Object object, Type type);
}
