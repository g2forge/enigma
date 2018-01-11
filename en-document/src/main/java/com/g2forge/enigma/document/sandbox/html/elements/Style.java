package com.g2forge.enigma.document.sandbox.html.elements;

import com.g2forge.enigma.document.sandbox.html.HTMLField;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

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