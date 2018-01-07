package com.g2forge.enigma.document.sandbox.wikitext.doc;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.enigma.document.sandbox.wikitext.Text;

public class TextBuilder extends AWikitextBuilder<Text> {
	protected final StringBuilder builder = new StringBuilder();

	public TextBuilder(IFunction1<? super Text, ? extends IWikitextBuilder<?>> closer) {
		super(closer);
	}

	@Override
	public Text build() {
		return new Text(builder.toString());
	}

	@Override
	public IWikitextBuilder<Text> characters(String text) {
		ensureOpen();
		builder.append(text);
		return this;
	}
}
