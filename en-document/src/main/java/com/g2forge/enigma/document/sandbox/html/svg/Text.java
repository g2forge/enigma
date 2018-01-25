package com.g2forge.enigma.document.sandbox.html.svg;

import com.g2forge.enigma.document.sandbox.css.ICSSStyle;
import com.g2forge.enigma.document.sandbox.html.HTMLField;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Text implements ISVGElement, IReflectiveHTMLElement {
	protected final int x;

	protected final int y;

	protected final ICSSStyle style;

	@HTMLField(property = false)
	protected final String text;
}