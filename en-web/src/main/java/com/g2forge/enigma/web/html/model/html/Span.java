package com.g2forge.enigma.web.html.model.html;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
@HTMLTag(pretty = HTMLTag.Pretty.Inline)
public class Span implements IBodyElement, IReflectiveHTMLElement {
	protected final String id;

	@HTMLField(property = false)
	@Singular
	protected final Collection<?> contents;

	public Span(String id, Object... contents) {
		this(id, HCollection.asList(contents));
	}
}
