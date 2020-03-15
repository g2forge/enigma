package com.g2forge.enigma.bash.model.expression;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.alexandria.java.text.quote.QuoteControl;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashString implements IBashExpression {
	protected final BashQuoteType quoteType;

	protected final QuoteControl quoteControl;

	@Singular
	protected final List<Object> elements;

	public BashString(BashQuoteType quoteType, QuoteControl quoteControl, Object... elements) {
		this(quoteType, quoteControl, HCollection.asList(elements));
	}

	public BashString(Object... elements) {
		this(BashQuoteType.BashDoubleExpand, QuoteControl.IfNeeded, HCollection.asList(elements));
	}
}
