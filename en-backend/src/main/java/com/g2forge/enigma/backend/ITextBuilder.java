package com.g2forge.enigma.backend;

public interface ITextBuilder<T> extends ITextAppender<T> {
	public T newline();
}
