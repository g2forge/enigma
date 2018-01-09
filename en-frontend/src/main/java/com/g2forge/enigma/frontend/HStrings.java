package com.g2forge.enigma.frontend;

import com.g2forge.alexandria.java.core.helpers.HString;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HStrings {
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

	public static String unwrap(String string) {
		if (string.length() < 2) throw new IllegalArgumentException("String \"" + string + "\" is not long enough to be wrapped in quotes!");
		for (Quotes quotes : Quotes.values()) {
			if (quotes.is(string)) return HString.unescape(string.substring(1, string.length() - 1));
		}
		throw new IllegalArgumentException("String \"" + string + "\" is not wrapped in matching quotes!");
	}
}
