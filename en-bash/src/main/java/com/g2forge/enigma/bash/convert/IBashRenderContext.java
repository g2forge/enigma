package com.g2forge.enigma.bash.convert;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.backend.ITextBuilder;
import com.g2forge.enigma.backend.convert.common.IRenderContext;

public interface IBashRenderContext extends IRenderContext<IBashRenderContext>, ITextBuilder<IBashRenderContext> {
	public ICloseable block();

	public ICloseable indent();

	public boolean isBlockMode();

	public ICloseable line();

	public ICloseable quote();

	public ICloseable raw();

	public ICloseable token(boolean quote);
}
