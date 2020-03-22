package com.g2forge.enigma.bash.template;

import com.g2forge.enigma.bash.convert.IBashRenderContext;

public interface IBashTemplateRenderContext extends IBashRenderContext {
	public String getValue(String variable);
}
