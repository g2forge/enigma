package com.g2forge.enigma.document.sandbox.html.html;

import com.g2forge.enigma.document.sandbox.css.ICSSStyle;
import com.g2forge.enigma.document.sandbox.html.HTMLTag;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

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
