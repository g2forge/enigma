package com.g2forge.enigma.bash.convert.textmodifiers;

import java.util.List;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.enigma.backend.model.modifier.ITextModifier;
import com.g2forge.enigma.backend.model.modifier.TextUpdate;

public class BashTokenModifier implements ITextModifier, ISingleton {
	protected static final BashTokenModifier INSTANCE = new BashTokenModifier();

	public static BashTokenModifier create() {
		return INSTANCE;
	}

	@Override
	public List<List<TextUpdate>> computeUpdates(List<CharSequence> list) {
		if (isRequiresQuote(list)) return BashDoubleQuoteModifier.create().computeUpdates(list);
		return null;
	}

	protected boolean isRequiresQuote(List<CharSequence> list) {
		// If there's a gap in the middle, always quote since we never know what someone will put in there...
		if (list.size() > 1) return true;
		return BashQuoteType.BashDoubleExpand.isQuoteNeeded(HCollection.getOne(list));
	}

	@Override
	public ITextModifier merge(Iterable<? extends ITextModifier> modifiers) {
		throw new NotYetImplementedError();
	}
}
