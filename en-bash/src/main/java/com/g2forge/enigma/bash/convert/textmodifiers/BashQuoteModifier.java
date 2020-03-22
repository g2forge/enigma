package com.g2forge.enigma.bash.convert.textmodifiers;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.TextUpdate;
import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.enigma.backend.text.model.modifier.ITextModifier;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashQuoteModifier implements ITextModifier {
	protected final BashQuoteType quoteType;

	@Override
	public List<? extends List<? extends TextUpdate<?>>> computeUpdates(List<CharSequence> list) {
		final List<List<TextUpdate<?>>> retVal = new ArrayList<>(list.size());
		for (int i = 0; i < list.size(); i++) {
			final List<TextUpdate<?>> updates = new ArrayList<>();
			if (i == 0) updates.add(new TextUpdate<>(0, 0, IFunction1.create(quoteType.getPrefix())));
			quoteType.getEscapeType().getEscaper().computeEscape(list.get(i).toString(), update -> {
				if (update.getFunction() instanceof IFunction1.Identity) return;
				updates.add(update);
			});
			if (i == list.size() - 1) updates.add(new TextUpdate<>(HCollection.getLast(list).length(), 0, IFunction1.create(quoteType.getPostfix())));
			retVal.add(updates.isEmpty() ? null : updates);
		}
		return retVal;
	}

	@Override
	public ITextModifier merge(Iterable<? extends ITextModifier> modifiers) {
		throw new NotYetImplementedError();
	}
}
