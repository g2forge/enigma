package com.g2forge.enigma.web.html.model.html.list;

import java.util.Arrays;
import java.util.Collection;

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
@HTMLTag("li")
public class ListItem implements IReflectiveHTMLElement {
	@HTMLField(property = false)
	@Singular
	protected final Collection<?> elements;

	public ListItem(Object... elements) {
		this(Arrays.asList(elements));
	}
}
