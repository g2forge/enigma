package com.g2forge.enigma.document.convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.mylyn.wikitext.markdown.MarkdownLanguage;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.parser.markup.MarkupLanguage;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.enigma.document.Block;

import lombok.Data;
import lombok.Getter;

@Data
public class WikitextParser {
	@Getter(lazy = true)
	private static final WikitextParser markdown = new WikitextParser(new MarkdownLanguage());

	protected final MarkupLanguage language;

	protected Block parse(IConsumer1<MarkupParser> consumer) {
		final WikitextDocumentBuilder builder = new WikitextDocumentBuilder();
		final MarkupParser parser = new MarkupParser(getLanguage(), builder);
		consumer.accept(parser);
		return builder.getDocument();
	}

	public Block parse(Path path) throws IOException {
		return parse(parser -> {
			try (final BufferedReader reader = Files.newBufferedReader(path)) {
				parser.parse(reader);
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
		});
	}

	public Block parse(Reader content) throws IOException {
		return parse(parser -> {
			try {
				parser.parse(content);
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
		});
	}

	public Block parse(String content) {
		return parse(parser -> parser.parse(content));
	}
}
