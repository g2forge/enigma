package com.g2forge.enigma.web.html.model.html;

import com.g2forge.enigma.web.css.model.ICSSStyle;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@HTMLTag("img")
public class Image implements IBodyElement, IReflectiveHTMLElement {
	protected final String src;

	protected final ICSSStyle style;
}
