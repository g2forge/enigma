package com.g2forge.enigma.web.html.model.svg;

import com.g2forge.enigma.web.css.model.ICSSStyle;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

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