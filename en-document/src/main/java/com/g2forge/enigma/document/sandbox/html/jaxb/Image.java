package com.g2forge.enigma.document.sandbox.html.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "img")
@XmlAccessorType(XmlAccessType.FIELD)
public class Image implements IBodyElement {
	@XmlAttribute
	protected String src;
	
	@XmlAttribute
	protected String style;
}
