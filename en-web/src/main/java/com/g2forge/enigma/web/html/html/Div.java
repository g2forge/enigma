package com.g2forge.enigma.web.html.html;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.HTMLField;
import com.g2forge.enigma.web.html.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Div implements IBodyElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<IBodyElement> elements;

	public Div(IBodyElement... elements) {
		this(Arrays.asList(elements));
	}
}