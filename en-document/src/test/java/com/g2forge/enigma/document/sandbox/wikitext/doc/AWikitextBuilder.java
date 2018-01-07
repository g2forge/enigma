package com.g2forge.enigma.document.sandbox.wikitext.doc;

import java.util.function.Consumer;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public abstract class AWikitextBuilder<T> implements IWikitextBuilder<T> {
	protected boolean closed = false;

	protected final IFunction1<? super T, ? extends IWikitextBuilder<?>> closer;

	protected IWikitextBuilder<?> close() {
		ensureOpen();
		setClosed(true);
		final IFunction1<? super T, ? extends IWikitextBuilder<?>> closer = getCloser();
		return closer == null ? null : closer.apply(build());
	}

	protected <_T> IFunction1<_T, IWikitextBuilder<T>> closer(Consumer<_T> consumer) {
		return input -> {
			consumer.accept(input);
			return this;
		};
	}

	protected void ensureOpen() {
		if (isClosed()) throw new IllegalStateException();
	}
}
