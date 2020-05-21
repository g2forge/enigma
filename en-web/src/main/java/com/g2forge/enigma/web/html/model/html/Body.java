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
public class Body implements IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<IBodyElement> elements;

	public Body(IBodyElement... elements) {
		this(Arrays.asList(elements));
	}
}