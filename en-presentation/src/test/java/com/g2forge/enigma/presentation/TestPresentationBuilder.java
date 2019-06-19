package com.g2forge.enigma.presentation;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.io.file.HZip;
import com.g2forge.alexandria.java.io.file.TempDirectory;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.DocList;
import com.g2forge.enigma.document.Emphasis;
import com.g2forge.enigma.document.IBlock;
import com.g2forge.enigma.document.Link;
import com.g2forge.enigma.document.Text;
import com.g2forge.enigma.presentation.content.ContentDoc;
import com.g2forge.enigma.presentation.slide.SlideContent1;
import com.g2forge.enigma.presentation.slide.SlideTitle;

public class TestPresentationBuilder {
	protected void assertPresentationEquals(final String expected, final IConsumer1<PresentationBuilder> test) throws IOException {
		try (final TempDirectory temp = new TempDirectory()) {
			try (final PresentationBuilder actual = new PresentationBuilder()) {
				final Path actualActual = temp.get().resolve("actual.pptx");
				final Path expectedPath = temp.getResource().resource(getClass(), expected, "expected.pptx");

				test.accept(actual);
				actual.write(actualActual);
				Assert.assertTrue(HZip.isEqual(actualActual, expectedPath));
			}
		}
	}

	@Test
	public void code() throws IOException {
		assertPresentationEquals("code.pptx", presentation -> {
			final IBlock content = Block.builder().type(Block.Type.Paragraph).content(new Text("Hello ")).content(new Emphasis(Emphasis.Type.Code, new Text("world"))).content(new Text("!")).build();
			presentation.add(new SlideContent1("Title", "Subtitle", new ContentDoc(content)));
		});
	}

	@Test
	public void doc() throws IOException {
		assertPresentationEquals("doc.pptx", presentation -> {
			final DocList.DocListBuilder list = DocList.builder().marker(DocList.Marker.Numbered);
			list.item(new Text("First"));
			list.item(new Emphasis(Emphasis.Type.Strong, new Text("Second")));
			presentation.add(new SlideContent1("Title", "Subtitle", new ContentDoc(list.build())));
		});
	}

	@Test
	public void empty() throws IOException {
		assertPresentationEquals("empty.pptx", presentation -> {});
	}

	@Test
	public void link() throws IOException {
		assertPresentationEquals("link.pptx", presentation -> {
			presentation.add(new SlideContent1("Title", "Subtitle", new ContentDoc(new Link("http://g2forge.com", new Text("G2Forge")))));
		});
	}

	@Test
	public void title() throws IOException {
		assertPresentationEquals("title.pptx", presentation -> presentation.add(new SlideTitle("Title", "Subtitle")));
	}
}
