package com.g2forge.enigma.web.html.model.html;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@HTMLTag("a")
public class Anchor implements IBodyElement, IReflectiveHTMLElement {
	protected final String href;

	@HTMLField(property = false)
	@Singular
	protected final Collection<?> elements;

	public Anchor(String href, Object... elements) {
		this(href, Arrays.asList(elements));
	}
}
