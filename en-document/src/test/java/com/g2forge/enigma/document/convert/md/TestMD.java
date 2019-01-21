package com.g2forge.enigma.document.convert.md;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.resource.HResource;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.Block.BlockBuilder;
import com.g2forge.enigma.document.Definition;
import com.g2forge.enigma.document.DocList;
import com.g2forge.enigma.document.Emphasis;
import com.g2forge.enigma.document.Text;

public class TestMD {
	protected final MDRenderer renderer = new MDRenderer();

	@Test
	public void help() {
		final DocList.DocListBuilder builder = DocList.builder().marker(DocList.Marker.Ordered);
		builder.item(Definition.builder().term(new Emphasis(Emphasis.Type.Code, new Text("-x"))).body(new Text("This is a sentence about the 'x' option!")).build());
		builder.item(Definition.builder().term(new Emphasis(Emphasis.Type.Code, new Text("-y"))).body(new Text("Some description of another option")).build());
		final DocList actual = builder.build();

		final String expected = HResource.read(getClass(), "help.md", true);
		Assert.assertEquals(expected, renderer.render(actual));
	}

	@Test
	public void simple() {
		final BlockBuilder builder = Block.builder().type(Block.Type.Block);
		builder.content(DocList.builder().marker(DocList.Marker.Ordered).item(new Text("Item 1")).build());
		builder.content(new Text("A paragraph goes here."));
		builder.content(DocList.builder().marker(DocList.Marker.Numbered).item(new Text("Item 2")).item(new Text("Item 3")).build());
		final Block actual = builder.build();

		final String expected = HResource.read(getClass(), "simple.md", true);
		Assert.assertEquals(expected, renderer.render(actual));
	}
}
