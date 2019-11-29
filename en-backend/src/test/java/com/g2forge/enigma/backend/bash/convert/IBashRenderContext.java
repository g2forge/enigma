package com.g2forge.enigma.backend.bash.convert;

import com.g2forge.enigma.backend.ITextBuilder;
import com.g2forge.enigma.backend.convert.common.IRenderContext;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified;

public interface IBashRenderContext extends IRenderContext<IBashRenderContext>, ITextBuilder<IBashRenderContext> {
	public TextNestedModified.IModifierHandle indent();

	public TextNestedModified.IModifierHandle token();

	public TextNestedModified.IModifierHandle raw();
}
