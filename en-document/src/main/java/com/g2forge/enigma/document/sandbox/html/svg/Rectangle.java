package com.g2forge.enigma.document.sandbox.html.svg;

import com.g2forge.enigma.document.sandbox.css.ICSSStyle;
import com.g2forge.enigma.document.sandbox.html.HTMLTag;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

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