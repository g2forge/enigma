package com.g2forge.enigma.document.sandbox.html.custom;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Span implements IReflectiveHTMLElement {
	protected final String id;

	@HTMLField(property = false)
	@Singular
	protected final Collection<?> contents;

	public Span(String id, Object... contents) {
		this(id, HCollection.asList(contents));
	}
}
