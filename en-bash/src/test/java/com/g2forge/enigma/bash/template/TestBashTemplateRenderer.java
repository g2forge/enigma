package com.g2forge.enigma.bash.template;

import org.junit.Test;

import com.g2forge.alexandria.adt.associative.map.HMap;
import com.g2forge.alexandria.adt.associative.map.MapBuilder;
import com.g2forge.alexandria.java.adt.name.IStringNamed;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.expression.BashExpansion;
import com.g2forge.enigma.bash.model.statement.BashCommand;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class TestBashTemplateRenderer {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Substitution implements IExplicitBashTemplateRenderable, IStringNamed {
		protected final String name;

		@Override
		public void render(IBashTemplateRenderContext context) {
			final String value = context.getValue(getName());
			if (value != null) context.render(value, String.class);
			else context.render(new BashExpansion(getName()), BashExpansion.class);
		}
	}

	@Test
	public void specified() {
		final IExplicitBashTemplateRenderable template = new Substitution("x");
		final BashTemplateRenderer renderer = new BashTemplateRenderer(BashRenderer.Mode.Line, new MapBuilder<String, String>().put("x", "Hello, World!").build());
		HAssert.assertEquals("echo \"Hello, World!\"", renderer.render(new BashCommand("echo", template)));
	}

	@Test
	public void unspecified() {
		final IExplicitBashTemplateRenderable template = new Substitution("x");
		final BashTemplateRenderer renderer = new BashTemplateRenderer(BashRenderer.Mode.Line, HMap.empty());
		HAssert.assertEquals("echo \"${x}\"", renderer.render(new BashCommand("echo", template)));
	}
}
