package com.g2forge.enigma.web.html.html;

import com.g2forge.alexandria.java.core.iface.ISingleton;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

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
