package com.g2forge.enigma.document.sandbox.html.elements;

import com.g2forge.alexandria.java.core.iface.ISingleton;
import com.g2forge.enigma.document.sandbox.html.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BR implements IBodyElement, IReflectiveHTMLElement, ISingleton {
	protected static final BR singleton = new BR();

	public static BR create() {
		return singleton;
	}
}
