package com.g2forge.enigma.web.html.model.html.list;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;
import com.g2forge.enigma.web.html.model.html.IBodyElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
@HTMLTag("ul")
public class UnorderedList implements IBodyElement, IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<ListItem> items;

	public UnorderedList(ListItem... items) {
		this(Arrays.asList(items));
	}
}
