package com.g2forge.enigma.backend.bash.convert;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.backend.ITextBuilder;
import com.g2forge.enigma.backend.convert.common.IRenderContext;

public interface IBashRenderContext extends IRenderContext<IBashRenderContext>, ITextBuilder<IBashRenderContext> {
	public ICloseable indent();

	public ICloseable token();
}
