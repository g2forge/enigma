package com.g2forge.enigma.document.convert.md;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HResource;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.Block.BlockBuilder;
import com.g2forge.enigma.document.List;
import com.g2forge.enigma.document.Text;

public class TestMD {
	protected final MDRenderer renderer = new MDRenderer();

	@Test
	public void simple() {
		final BlockBuilder builder = Block.builder();
		builder.content(List.builder().marker(List.Marker.Ordered).item(new Text("Item 1")).build());
		builder.content(new Text("A paragraph goes here."));
		builder.content(List.builder().marker(List.Marker.Numbered).item(new Text("Item 2")).item(new Text("Item 3")).build());
		final Block actual = builder.build();

		final String expected = HResource.read(getClass(), "simple.md").replace(System.lineSeparator(), "\n");
		Assert.assertEquals(expected, renderer.render(actual));
	}
}
