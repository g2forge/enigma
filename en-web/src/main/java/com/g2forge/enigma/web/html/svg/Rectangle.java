package com.g2forge.enigma.web.html.svg;

import com.g2forge.enigma.web.css.ICSSStyle;
import com.g2forge.enigma.web.html.HTMLTag;
import com.g2forge.enigma.web.html.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@HTMLTag("rect")
public class Rectangle implements ISVGElement, IReflectiveHTMLElement {
	protected final int x;

	protected final int y;

	protected final int width;

	protected final int height;

	protected final ICSSStyle style;
}