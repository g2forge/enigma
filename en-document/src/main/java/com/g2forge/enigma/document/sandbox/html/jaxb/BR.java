package com.g2forge.enigma.document.sandbox.html.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.g2forge.alexandria.java.core.iface.ISingleton;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BR implements IBodyElement, ISingleton {
	protected static final BR singleton = new BR();

	public static BR create() {
		return singleton;
	}
}
