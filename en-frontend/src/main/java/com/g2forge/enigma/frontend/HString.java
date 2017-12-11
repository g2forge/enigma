package com.g2forge.enigma.frontend;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HString {
	@AllArgsConstructor
	@Getter
	protected enum Quotes {
		Smart('“', '”'),
		Plain('"', '"');

		protected final char open;

		protected final char close;

		public boolean is(String string) {
			return (string.charAt(0) == getOpen()) && (string.charAt(string.length() - 1) == getClose());
		}
	}

	public static String unescape(String string) throws NotYetImplementedError {
		final StringBuilder retVal = new StringBuilder(string.length());
		for (int i = 0; i < string.length(); i++) {
			final char current = string.charAt(i);
			if (current == '\\') {
				switch (string.charAt(i + 1)) {
					case '“':
					case '”':
					case '\\':
						retVal.append(string.charAt(i + 1));
						break;
					case 'n':
						retVal.append('\n');
						break;
					case 'r':
						retVal.append('\r');
						break;
					default:
						throw new NotYetImplementedError();
				}
				i++;
			} else retVal.append(current);
		}
		return retVal.toString();
	}

	public static String unwrap(String string) {
		if (string.length() < 2) throw new IllegalArgumentException("String \"" + string + "\" is not long enough to be wrapped in quotes!");
		for (Quotes quotes : Quotes.values()) {
			if (quotes.is(string)) return unescape(string.substring(1, string.length() - 1));
		}
		throw new IllegalArgumentException("String \"" + string + "\" is not wrapped in matching quotes!");
	}
}
