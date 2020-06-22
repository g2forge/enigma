package com.g2forge.enigma.web.html.model.html;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BR implements IBodyElement, IReflectiveHTMLElement, ISingleton {
	protected static final BR singleton = new BR();

	public static BR create() {
		return singleton;
	}
}
