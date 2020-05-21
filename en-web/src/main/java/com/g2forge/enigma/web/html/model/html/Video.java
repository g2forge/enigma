package com.g2forge.enigma.web.html.model.html;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Video implements IBodyElement, IReflectiveHTMLElement {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Source implements IReflectiveHTMLElement {
		protected final String source;

		protected final String type;
	}

	protected final int width;

	protected final int height;

	protected final boolean controls;

	@HTMLField(property = false)
	@Singular
	protected final Collection<Source> sources;

	public Video(int width, int height, boolean controls, Source... sources) {
		this(width, height, controls, Arrays.asList(sources));
	}
}
