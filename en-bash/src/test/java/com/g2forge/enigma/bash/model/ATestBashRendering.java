package com.g2forge.enigma.bash.model;

import java.util.List;

import com.g2forge.enigma.bash.convert.BashRenderer;
import com.g2forge.enigma.bash.model.statement.BashCommand;

import lombok.Getter;

public abstract class ATestBashRendering {
	@Getter(lazy = true)
	private static final BashRenderer lineRenderer = new BashRenderer(BashRenderer.Mode.Line);

	@Getter(lazy = true)
	private static final BashRenderer blockRenderer = new BashRenderer(BashRenderer.Mode.Block);

	protected static String toBlock(final Object object) {
		return getBlockRenderer().render(object);
	}

	protected static String toLine(final Object object) {
		return getLineRenderer().render(object);
	}

	protected static List<String> toTokens(BashCommand command) {
		return BashRenderer.toTokens(command);
	}
}
