package com.g2forge.enigma.web.html.model.svg;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@HTMLTag("g")
public class Group implements ISVGElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final Collection<ISVGElement> elements;

	public Group(ISVGElement... elements) {
		this(Arrays.asList(elements));
	}
}