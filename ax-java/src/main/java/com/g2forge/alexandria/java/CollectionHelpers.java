package com.g2forge.alexandria.java;

import java.util.Collections;
import java.util.List;

public class CollectionHelpers {
	@SuppressWarnings("unchecked")
	public static <T> List<T> getEmpty() {
		return Collections.EMPTY_LIST;
	}
}
