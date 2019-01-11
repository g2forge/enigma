package com.g2forge.enigma.web.html.html;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Title implements IHeadElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final String text;
}