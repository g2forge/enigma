package com.g2forge.enigma.backend.model.modifier;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.enigma.backend.model.expression.ITextExpression;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class IndentTextModifier implements ITextModifier {
	protected final ITextExpression indent;

	@Override
	public List<List<TextUpdate>> computeUpdates(List<CharSequence> list) {
		final List<List<TextUpdate>> retVal = new ArrayList<>(list.size());
		final IFunction1<CharSequence, ITextExpression> function = IFunction1.create(getIndent());
		for (CharSequence sequence : list) {
			final List<TextUpdate> updates = new ArrayList<>();
			retVal.add(updates);

			boolean found = false;
			for (int i = 0; i < sequence.length(); i++) {
				final char character = sequence.charAt(i);
				if ((character == '\n') || (character == '\r')) found = true;
				else if (found) {
					updates.add(new TextUpdate(i, 0, function));
					found = false;
				}
			}
		}
		return retVal;
	}

	@Override
	public ITextModifier merge(Iterable<? extends ITextModifier> modifiers) {
		throw new NotYetImplementedError();
	}
}
