package com.g2forge.enigma.document.sandbox.html.html;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.document.sandbox.html.HTMLField;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Body implements IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<IBodyElement> elements;

	public Body(IBodyElement... elements) {
		this(Arrays.asList(elements));
	}
}