package com.g2forge.enigma.backend.text.convert;

import java.util.List;

import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.text.TextUpdate;
import com.g2forge.enigma.backend.convert.IRenderContext;

public interface ITextRenderContext extends IRenderContext<Object, ITextRenderContext>, IBuilder<String> {
	public ITextRenderContext createContext();

	public StringBuilder getBuilder();

	/**
	 * @param string
	 * @param updates
	 * @param unchanged
	 * @return A list of the lengths of the updates.
	 */
	public List<Integer> modify(final String string, final List<? extends TextUpdate<?>> updates, int unchanged);
}
