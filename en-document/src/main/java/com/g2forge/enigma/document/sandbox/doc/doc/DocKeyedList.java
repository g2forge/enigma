package com.g2forge.enigma.document.sandbox.doc.doc;

import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocKeyedList<K extends IDocElement, V extends IDocElement> implements IDocKeyedList<K, V> {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Entry<K extends IDocElement, V extends IDocElement> implements IDocKeyedList.IEntry<K, V> {
		protected K key;
		protected V value;
	}

	protected Collection<IDocKeyedList.IEntry<K, V>> elements;

	public DocKeyedList<K, V> add(K key, V value) {
		if (elements == null) elements = new ArrayList<>();
		elements.add(new Entry<>(key, value));
		return this;
	}
}
