package com.g2forge.enigma.web.html.model.html;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@HTMLTag(pretty = HTMLTag.Pretty.NoIndent)
public class HTML implements IReflectiveHTMLElement {
	@HTMLField(property = false)
	protected final Head head;

	@HTMLField(property = false)
	protected final Body body;
}