package com.g2forge.enigma.document.sandbox.html.custom;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Span implements IReflectiveHTMLElement {
	protected String id;

	@HTMLField(property = false)
	protected Collection<?> contents;

	public Span(Collection<?> contents) {
		this.contents = contents;
	}

	public Span(Object... contents) {
		this(HCollection.asList(contents));
	}

	public Span setId(String id) {
		this.id = id;
		return this;
	}
}
