package com.g2forge.enigma.web.html;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;
import com.g2forge.enigma.web.html.convert.HTMLRenderer;
import com.g2forge.enigma.web.html.html.Table;

public class TestTable {
	protected static final HTMLRenderer renderer = new HTMLRenderer();

	protected static String render(Object object) {
		return renderer.render(object).replaceAll("[\n\t]+", "");
	}

	@Test
	public void basic() {
		final Table.TableBuilder table = Table.builder();
		table.row(Table.Row.builder().cell(Table.Header.EMPTY).cell(new Table.Header("A")).cell(new Table.Header("B")).build());
		table.row(Table.Row.builder().cell(new Table.Data("1")).cell(new Table.Data("A1")).cell(new Table.Data("B1")).build());
		table.row(Table.Row.builder().cell(new Table.Data("2")).cell(new Table.Data("A2")).cell(new Table.Data("B2")).build());
		HAssert.assertEquals("<table><tr><th/><th>A</th><th>B</th></tr><tr><td>1</td><td>A1</td><td>B1</td></tr><tr><td>2</td><td>A2</td><td>B2</td></tr></table>", render(table.build()));
	}

	@Test
	public void empty() {
		HAssert.assertEquals("<table/>", renderer.render(Table.builder().build()));
	}

	@Test
	public void header() {
		final Table.TableBuilder table = Table.builder();
		table.row(Table.Row.builder().cell(new Table.Header("Hello")).build());
		HAssert.assertEquals("<table><tr><th>Hello</th></tr></table>", render(table.build()));
	}
}
