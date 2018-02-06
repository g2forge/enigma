package com.g2forge.enigma.web.html.html;

import java.util.Arrays;
import java.util.Collection;

import com.g2forge.enigma.web.html.convert.HTMLField;
import com.g2forge.enigma.web.html.convert.HTMLTag;
import com.g2forge.enigma.web.html.convert.IReflectiveHTMLElement;

import lombok.AllArgsConstructor;
import lombok.Builder;

@lombok.Data
@Builder
@AllArgsConstructor
public class Table implements IBodyElement, IReflectiveHTMLElement {
	@lombok.Data
	@Builder
	@AllArgsConstructor
	@HTMLTag("td")
	public static class Data implements ICell, IReflectiveHTMLElement {
		@HTMLField(property = false)
		protected final Collection<IBodyElement> elements;

		public Data(IBodyElement... elements) {
			this(Arrays.asList(elements));
		}
	}

	@lombok.Data
	@Builder
	@AllArgsConstructor
	@HTMLTag("th")
	public static class Header implements ICell, IReflectiveHTMLElement {
		@HTMLField(property = false)
		protected final Collection<IBodyElement> elements;

		public Header(IBodyElement... elements) {
			this(Arrays.asList(elements));
		}
	}

	public static interface ICell {}

	@lombok.Data
	@Builder
	@AllArgsConstructor
	@HTMLTag("tr")
	public static class Row implements IReflectiveHTMLElement {
		@HTMLField(property = false)
		protected Collection<ICell> cells;

		public Row(ICell... cells) {
			this(Arrays.asList(cells));
		}
	}

	protected String style;

	@HTMLField(property = false)
	protected Collection<Row> rows;

	public Table(String style, Row... rows) {
		this(style, Arrays.asList(rows));
	}
}
