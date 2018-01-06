package com.g2forge.enigma.document.sandbox.html.jaxb;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Video implements IBodyElement {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Source {
		@XmlAttribute
		protected String source;

		@XmlAttribute
		protected String type;
	}

	@XmlAttribute
	protected int width;

	@XmlAttribute
	protected int height;

	@XmlAttribute
	protected boolean controls;

	@XmlAnyElement
	protected Collection<Source> rows;

	public Video(int width, int height, boolean controls, Source... sources) {
		this(width, height, controls, Arrays.asList(sources));
	}
}
