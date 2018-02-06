package com.g2forge.enigma.web.html.html;

import com.g2forge.enigma.web.css.ICSSStyle;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@HTMLTag("img")
public class Image implements IBodyElement, IReflectiveHTMLElement {
	protected final String src;

	protected final ICSSStyle style;
}
