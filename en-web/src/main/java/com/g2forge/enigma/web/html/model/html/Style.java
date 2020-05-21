package com.g2forge.enigma.web.html.model.html;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Style implements IHeadElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final String text;
}