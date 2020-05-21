package com.g2forge.enigma.web.html.model.html;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
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
@HTMLTag(value = "p", pretty = HTMLTag.Pretty.Inline)
public class Paragraph implements IBodyElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<?> contents;

	public Paragraph(Object... contents) {
		this(HCollection.asList(contents));
	}
}
