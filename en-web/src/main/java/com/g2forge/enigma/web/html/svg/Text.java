package com.g2forge.enigma.web.html.svg;

import com.g2forge.enigma.web.css.ICSSStyle;
import com.g2forge.enigma.web.html.HTMLField;
import com.g2forge.enigma.web.html.IReflectiveHTMLElement;

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