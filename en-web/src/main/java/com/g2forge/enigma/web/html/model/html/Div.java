package com.g2forge.enigma.web.html.model.html;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Div implements IBodyElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<?> elements;

	public Div(Object... elements) {
		this(Arrays.asList(elements));
	}
}