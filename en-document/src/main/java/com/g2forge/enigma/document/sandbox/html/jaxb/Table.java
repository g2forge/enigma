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
public class Table implements IBodyElement {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement(name = "td")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Cell {
		@XmlAnyElement
		protected Collection<IBodyElement> elements;

		public Cell(IBodyElement... elements) {
			this(Arrays.asList(elements));
		}
	}

	@XmlRootElement(name = "th")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Header extends Cell {
		public Header() {}

		public Header(Collection<IBodyElement> elements) {
			super(elements);
		}

		public Header(IBodyElement... elements) {
			super(elements);
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlRootElement(name = "tr")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Row {
		@XmlAnyElement
		protected Collection<Cell> cells;

		public Row(Cell... cells) {
			this(Arrays.asList(cells));
		}
	}

	@XmlAttribute
	protected String style;

	@XmlAnyElement
	protected Collection<Row> rows;

	public Table(String style, Row... rows) {
		this(style, Arrays.asList(rows));
	}
}
