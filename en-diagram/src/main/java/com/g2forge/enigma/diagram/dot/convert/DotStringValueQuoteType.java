package com.g2forge.enigma.diagram.dot.convert;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.text.escape.EscapeType;
import com.g2forge.alexandria.java.text.escape.IEscapeType;
import com.g2forge.alexandria.java.text.escape.SingleCharacterEscaper;
import com.g2forge.alexandria.java.text.quote.IQuoteType;

public class DotStringValueQuoteType implements IQuoteType, ISingleton {
	protected static final DotStringValueQuoteType INSTANCE = new DotStringValueQuoteType();

	protected static final Pattern PATTERN = Pattern.compile("([_a-zA-Z\200-\377][_0-9a-zA-Z\200-\377]*)|(-?(\\.[0-9]+|[0-9]+(\\.[0-9]*)?))");

	public static DotStringValueQuoteType create() {
		return INSTANCE;
	}

	private DotStringValueQuoteType() {}

	@Override
	public IEscapeType getEscapeType() {
		return new EscapeType(new SingleCharacterEscaper("\\", null, "\"", null, -1));
	}

	@Override
	public String getPostfix() {
		return getPrefix();
	}

	@Override
	public String getPrefix() {
		return "\"";
	}

	@Override
	public boolean isQuoteNeeded(CharSequence string) {
		return !PATTERN.matcher(string).matches();
	}
}