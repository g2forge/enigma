package com.g2forge.enigma.record.core;

@FunctionalInterface
public interface IName<LN> {
	public LN getLanguageName(Context context);
}
