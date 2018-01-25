package com.g2forge.enigma.document.sandbox.html.svg;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.document.sandbox.html.HTMLField;
import com.g2forge.enigma.document.sandbox.html.HTMLTag;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@HTMLTag("g")
public class Group implements ISVGElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final Collection<ISVGElement> elements;

	public Group(ISVGElement... elements) {
		this(Arrays.asList(elements));
	}
}