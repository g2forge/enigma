package com.g2forge.enigma.web.html.model.html;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Head implements IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<IHeadElement> elements;

	public Head(IHeadElement... elements) {
		this(Arrays.asList(elements));
	}
}