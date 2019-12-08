package com.g2forge.enigma.bash.convert.textmodifiers;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.enigma.backend.model.modifier.ITextModifier;
import com.g2forge.enigma.backend.model.modifier.TextUpdate;

public class BashDoubleQuoteModifier implements ITextModifier, ISingleton {
	protected static final BashDoubleQuoteModifier INSTANCE = new BashDoubleQuoteModifier();

	public static BashDoubleQuoteModifier create() {
		return INSTANCE;
	}

	@Override
	public List<List<TextUpdate>> computeUpdates(List<CharSequence> list) {
		final List<List<TextUpdate>> retVal = new ArrayList<>(list.size());
		for (int i = 0; i < list.size(); i++) {
			final List<TextUpdate> updates = new ArrayList<>();
			if (i == 0) updates.add(new TextUpdate(0, 0, IFunction1.create("\"")));
			if (i == list.size() - 1) updates.add(new TextUpdate(HCollection.getLast(list).length(), 0, IFunction1.create("\"")));
			retVal.add(updates.isEmpty() ? null : updates);
		}
		return retVal;
	}

	@Override
	public ITextModifier merge(Iterable<? extends ITextModifier> modifiers) {
		throw new NotYetImplementedError();
	}
}
