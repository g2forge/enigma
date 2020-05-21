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
public class Div implements IBodyElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<?> elements;

	public Div(Object... elements) {
		this(Arrays.asList(elements));
	}
}