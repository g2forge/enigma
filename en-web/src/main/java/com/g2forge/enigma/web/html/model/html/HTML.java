package com.g2forge.enigma.web.html.model.html;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@HTMLTag(pretty = HTMLTag.Pretty.NoIndent)
public class HTML implements IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final Head head;

	@HTMLField(property = false)
	protected final Body body;
}