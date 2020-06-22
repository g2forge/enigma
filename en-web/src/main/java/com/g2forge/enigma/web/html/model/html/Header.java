package com.g2forge.enigma.web.html.model.html;

import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLIgnore;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IHTMLTagGenerator;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@HTMLTag(generator = Header.TagGenerator.class, pretty = HTMLTag.Pretty.Inline)
public class Header implements IBodyElement, IReflectiveHTMLElement {
	public static class TagGenerator implements IHTMLTagGenerator {
		@Override
		public String apply(IReflectiveHTMLElement header) {
			return "h" + ((Header) header).getLevel();
		}
	}

	protected final String id;

	@HTMLIgnore
	protected final int level;

	@HTMLField(property = false)
	@Singular
	protected final Collection<?> contents;

	public Header(String id, int level, Object... contents) {
		this(id, level, HCollection.asList(contents));
	}
}
