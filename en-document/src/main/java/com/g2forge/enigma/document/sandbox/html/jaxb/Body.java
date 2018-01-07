package com.g2forge.enigma.document.sandbox.html.jaxb;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Body {
	@XmlAnyElement
	protected Collection<IBodyElement> collection;

	public Body(IBodyElement... elements) {
		this(Arrays.asList(elements));
	}
}