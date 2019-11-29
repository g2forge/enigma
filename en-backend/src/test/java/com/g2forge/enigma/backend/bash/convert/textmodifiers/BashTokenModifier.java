package com.g2forge.enigma.backend.bash.convert.textmodifiers;

import java.util.List;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.enigma.backend.model.modifier.ITextModifier;
import com.g2forge.enigma.backend.model.modifier.TextUpdate;

public class BashTokenModifier implements ITextModifier, ISingleton {
	protected static final String WHITESPACE = " \t\n\r";
	protected static final String OPCHARACTERS = "|&;()<>";
	protected static final String METACHARACTERS = WHITESPACE + OPCHARACTERS;
	
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
		
		boolean containsOpCharacters = false, containsNonOpCharacters = false;
		for (CharSequence sequence : list) {
			for (int i = 0; i < sequence.length(); i++) {
				final char character = sequence.charAt(i);
				// If we find whitespace this thing definitely needs to be quoted
				for (int j = 0; j < WHITESPACE.length(); j++) {
					if (WHITESPACE.charAt(j) == character) return true;
				}

				for (int j = 0; j < OPCHARACTERS.length(); j++) {
					if (OPCHARACTERS.charAt(j) == character) containsOpCharacters = true;
					else containsNonOpCharacters = true;
				}
				if (containsOpCharacters && containsNonOpCharacters) return true;
			}
		}
		return false;
	}

	@Override
	public ITextModifier merge(Iterable<? extends ITextModifier> modifiers) {
		throw new NotYetImplementedError();
	}
}
