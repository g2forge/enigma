package com.g2forge.enigma.bash.convert.textmodifiers;

import java.util.List;

import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.text.TextUpdate;
import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.alexandria.java.text.quote.QuoteControl;
import com.g2forge.enigma.backend.model.modifier.ITextModifier;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashTokenModifier implements ITextModifier {
	protected final BashQuoteType quoteType;

	protected final QuoteControl quoteControl;

	@Override
	public List<? extends List<? extends TextUpdate<?>>> computeUpdates(List<CharSequence> list) {
		switch (getQuoteControl()) {
			case Never:
				return null;
			case IfNotAlready:
			case IfNeeded:
				if (!isRequiresQuote(list)) return null;
			case Always:
				return new BashQuoteModifier(getQuoteType()).computeUpdates(list);
			default:
				throw new EnumException(QuoteControl.class, getQuoteControl());
		}
	}

	protected boolean isRequiresQuote(List<CharSequence> list) {
		// If there's a gap in the middle, always quote since we never know what someone will put in there...
		if (list.size() > 1) return true;
		return getQuoteType().isQuoteNeeded(HCollection.getOne(list));
	}

	@Override
	public ITextModifier merge(Iterable<? extends ITextModifier> modifiers) {
		throw new NotYetImplementedError();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
