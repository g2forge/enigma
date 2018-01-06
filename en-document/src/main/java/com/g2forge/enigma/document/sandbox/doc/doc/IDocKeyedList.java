package com.g2forge.enigma.document.sandbox.doc.doc;

public interface IDocKeyedList<K extends IDocElement, V extends IDocElement> extends IDocList<IDocKeyedList.IEntry<K, V>> {
	public interface IEntry<K extends IDocElement, V extends IDocElement> extends IDocElement {
		public K getKey();

		public V getValue();
	}
}
