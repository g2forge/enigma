package com.g2forge.enigma.document.convert;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.eclipse.mylyn.wikitext.markdown.MarkdownLanguage;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.parser.markup.MarkupLanguage;

import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.io.dataaccess.IDataSource;
import com.g2forge.alexandria.java.io.dataaccess.PathDataSource;
import com.g2forge.alexandria.java.io.dataaccess.StringDataSource;
import com.g2forge.alexandria.java.type.ref.ITypeRef;
import com.g2forge.enigma.document.model.Block;

import lombok.Data;
import lombok.Getter;

@Data
public class WikitextParser {
	@Getter(lazy = true)
	private static final WikitextParser markdown = new WikitextParser(new MarkdownLanguage());

	protected final MarkupLanguage language;

	public Block parse(IDataSource source) {
		final WikitextDocumentBuilder builder = new WikitextDocumentBuilder();
		final MarkupParser parser = new MarkupParser(getLanguage(), builder);
		try {
			parser.parse(source.getReader(ITypeRef.of(Reader.class)));
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		return builder.getDocument();
	}

	public Block parse(Path path) throws IOException {
		return parse(new PathDataSource(path, StandardOpenOption.READ));
	}

	public Block parse(String string) {
		return parse(new StringDataSource(string));
	}
}
