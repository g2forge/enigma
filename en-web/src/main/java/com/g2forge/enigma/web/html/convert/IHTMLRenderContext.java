package com.g2forge.enigma.web.html.convert;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.web.css.convert.ICSSRenderContext;

public interface IHTMLRenderContext extends ICSSRenderContext {
	public ICloseable openTag(String tag, boolean inline);
}
