package com.g2forge.alexandria.java.enums;

import lombok.Getter;

@Getter
public class EnumPrefixedString<E extends Enum<E>> {
	public static <E extends Enum<E>> EnumPrefixedString<E> parse(E unspecified, String separator, String string) {
		final int index = string.indexOf(separator);
		if (index < 0) return new EnumPrefixedString<>(unspecified, separator, string);
		else {
			final E value = EnumHelpers.valueOfInsensitive(unspecified.getDeclaringClass(), string.substring(0, index));
			return new EnumPrefixedString<>(value, separator, string.substring(index + separator.length()));
		}
	}

	protected final E value;

	protected final String separator;

	protected final String string;

	public EnumPrefixedString(E unspecified, String separator, String string) {
		this.value = unspecified;
		this.separator = separator;
		this.string = string;
	}

	@Override
	public String toString() {
		return value.toString() + separator + string;
	}
}
