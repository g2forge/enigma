package com.g2forge.enigma.document.sandbox.html.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.g2forge.alexandria.java.core.helpers.HArray;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HTML {
	public static Marshaller createMarshaller(Class<?>...classes) throws JAXBException, PropertyException {
		final JAXBContext jc = JAXBContext.newInstance(HArray.concatenate(HArray.create(HTML.class, Body.class, Image.class, BR.class, Div.class, Span.class, Table.class, Table.Row.class, Table.Cell.class, Video.class, Video.Source.class, Head.class, Style.class), classes));
		final Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		return marshaller;
	}
	
	@XmlElementRef
	protected Head head;

	@XmlElementRef
	protected Body body;
}