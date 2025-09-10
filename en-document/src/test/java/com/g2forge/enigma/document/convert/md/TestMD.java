package com.g2forge.enigma.document.convert.md;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.resource.HResource;
import com.g2forge.enigma.document.model.Block;
import com.g2forge.enigma.document.model.Definition;
import com.g2forge.enigma.document.model.DocList;
import com.g2forge.enigma.document.model.DocList.DocListBuilder;
import com.g2forge.enigma.document.model.Emphasis;
import com.g2forge.enigma.document.model.Text;
import com.g2forge.enigma.document.model.Block.BlockBuilder;

public class TestMD {
	protected final MDRenderer renderer = new MDRenderer();

	protected void assertEquals(final String filename, final Object actual) {
		final String expected = HResource.read(getClass(), filename, true);
		Assert.assertEquals(expected, renderer.render(actual));
	}

	@Test
	public void help() {
		final DocList.DocListBuilder builder = DocList.builder().marker(DocList.Marker.Ordered);
		builder.item(Definition.builder().term(new Emphasis(Emphasis.Type.Code, new Text("-x"))).body(new Text("This is a sentence about the 'x' option!")).build());
		builder.item(Definition.builder().term(new Emphasis(Emphasis.Type.Code, new Text("-y"))).body(new Text("Some description of another option")).build());
		assertEquals("help.md", builder.build());
	}

	@Test
	public void lists() {
		final BlockBuilder builder = Block.builder().type(Block.Type.Block);
		final DocListBuilder list0 = DocList.builder().marker(DocList.Marker.Ordered);
		list0.item(Block.builder().type(Block.Type.Block).content(new Text("Item 1 Line 1")).content(new Text("Item 1 Line 2")).build());
		
		final DocListBuilder list1 = DocList.builder().marker(DocList.Marker.Ordered);
		list1.item(new Text("Item 2A"));
		list1.item(new Text("Item 2B"));
		list0.item(Block.builder().type(Block.Type.Block).content(new Text("Item 2")).content(list1.build()).build());
		
		builder.content(list0.build());
		assertEquals("lists.md", builder.build());
	}
	
	@Test
	public void simple() {
		final BlockBuilder builder = Block.builder().type(Block.Type.Block);
		builder.content(DocList.builder().marker(DocList.Marker.Ordered).item(new Text("Item 1")).build());
		builder.content(new Text("A paragraph goes here."));
		builder.content(DocList.builder().marker(DocList.Marker.Numbered).item(new Text("Item 2")).item(new Text("Item 3")).build());
		assertEquals("simple.md", builder.build());
	}
}
