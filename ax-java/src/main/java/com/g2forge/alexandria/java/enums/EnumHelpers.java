package com.g2forge.alexandria.java.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumHelpers {
	public static <E extends Enum<E>> E valueOfInsensitive(Class<E> klass, String text) {
		final Map<String, E> map = new LinkedHashMap<>();
		for (E value : klass.getEnumConstants()) {
			if (map.put(value.toString().toLowerCase(), value) != null) throw new IllegalArgumentException("Cannot perform case insensitive matching on enum \"" + klass + "\" because the members are not unique without casing!");
		}
		return map.get(text.toLowerCase());
	}
}
