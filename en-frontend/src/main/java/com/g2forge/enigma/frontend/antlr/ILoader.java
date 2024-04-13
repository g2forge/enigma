package com.g2forge.enigma.frontend.antlr;

import org.antlr.v4.runtime.ANTLRErrorListener;

public interface ILoader<K, V> {
	public V load(K key, ANTLRErrorListener...errorListeners);
}
