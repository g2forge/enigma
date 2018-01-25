package com.g2forge.enigma.document.sandbox.html.svg;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.document.sandbox.html.HTMLField;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;
import com.g2forge.enigma.document.sandbox.html.html.IBodyElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SVG implements IBodyElement, IReflectiveHTMLElement {
	protected final int width;

	protected final int height;

	@HTMLField(property = false)
	protected final Collection<ISVGElement> elements;

	public SVG(int width, int height, ISVGElement... elements) {
		this(width, height, Arrays.asList(elements));
	}
}