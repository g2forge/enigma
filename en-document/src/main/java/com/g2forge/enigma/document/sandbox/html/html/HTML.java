package com.g2forge.enigma.document.sandbox.html.html;

import com.g2forge.enigma.document.sandbox.html.HTMLField;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HTML implements IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final Head head;

	@HTMLField(property = false)
	protected final Body body;
}