package com.g2forge.enigma.frontend.text;

import com.g2forge.alexandria.java.text.escape.IEscapeType;
import com.g2forge.alexandria.java.text.quote.HQuote;
import com.g2forge.alexandria.java.text.quote.IEnumQuoteType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnigmaQuoteType implements IEnumQuoteType<EnigmaQuoteType> {
	Dumb(HQuote.QUOTE_DOUBLE, HQuote.QUOTE_DOUBLE, EnigmaEscapeType.String),
	Smart("\u201c", "\u201d", EnigmaEscapeType.String);

	protected final String prefix;

	protected final String postfix;

	protected final IEscapeType escapeType;

	@Override
	public boolean isQuoteNeeded(CharSequence string) {
		return true;
	}
}