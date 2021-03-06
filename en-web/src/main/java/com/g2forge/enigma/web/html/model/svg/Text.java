package com.g2forge.enigma.web.html.model.svg;

import com.g2forge.enigma.web.css.model.ICSSStyle;
import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@HTMLTag(pretty = HTMLTag.Pretty.Inline)
public class Text implements ISVGElement, IReflectiveHTMLElement {
	protected final int x;

	protected final int y;

	protected final ICSSStyle style;

	@HTMLField(property = false)
	protected final String text;
}