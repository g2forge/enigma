package com.g2forge.enigma.web.html.model.html;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Title implements IHeadElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final String text;
}