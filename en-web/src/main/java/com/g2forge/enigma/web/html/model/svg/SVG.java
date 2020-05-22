package com.g2forge.enigma.web.html.model.svg;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;
import com.g2forge.enigma.web.html.model.html.IBodyElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class SVG implements IBodyElement, IReflectiveHTMLElement {
	protected final int width;

	protected final int height;

	@HTMLField(property = false)
	protected final Collection<ISVGElement> elements;

	public SVG(int width, int height, ISVGElement... elements) {
		this(width, height, Arrays.asList(elements));
	}
}