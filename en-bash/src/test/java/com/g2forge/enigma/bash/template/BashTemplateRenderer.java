package com.g2forge.enigma.bash.template;

import java.util.Map;

import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.convert.IBashRenderContext;

import lombok.AccessLevel;
import lombok.Getter;

public class BashTemplateRenderer extends BashRenderer {
	protected class BashTemplateRenderContext extends BashRenderContext implements IBashTemplateRenderContext {
		public BashTemplateRenderContext(TextNestedModified.TextNestedModifiedBuilder builder, Mode mode) {
			super(builder, mode);
		}

		@Override
		public String getValue(String variable) {
			return getValues().get(variable);
		}
	}

	protected static class BashTemplateRendering extends BashRendering {
		@Override
		protected void extend(TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super IBashRenderContext>> builder) {
			super.extend(builder);
			builder.add(IExplicitBashTemplateRenderable.class, e -> c -> e.render((IBashTemplateRenderContext) c));
		}
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final IRendering<Object, IBashRenderContext, IExplicitRenderable<? super IBashRenderContext>> renderingStatic = new BashTemplateRendering();

	@Getter(AccessLevel.PROTECTED)
	protected final Map<String, String> values;

	public BashTemplateRenderer(Mode mode, Map<String, String> values) {
		super(mode);
		this.values = values;
	}

	@Override
	protected BashRenderContext createContext(TextNestedModified.TextNestedModifiedBuilder builder) {
		return new BashTemplateRenderContext(builder, getMode());
	}

	@Override
	protected IRendering<? super Object, ? extends IBashRenderContext, ? extends IExplicitRenderable<? super IBashRenderContext>> getRendering() {
		return getRenderingStatic();
	}
}
