package com.g2forge.enigma.bash.template;

import java.util.Map;

import com.g2forge.enigma.bash.convert.BashRenderer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashTemplateRenderer extends BashRenderer {
	public class BashTemplateRenderContext extends BashRenderContext implements IBashTemplateRenderContext {
		public BashTemplateRenderContext(Mode mode) {
			super(mode);
		}

		@Override
		public String getValue(String variable) {
			return getValues().get(variable);
		}
	}

	@Singular
	@Getter(AccessLevel.PROTECTED)
	protected final Map<String, String> values;

	@Override
	protected BashRenderContext createContext() {
		return new BashTemplateRenderContext(getMode());
	}
}
