package com.g2forge.enigma.frontend.antlr;

import java.io.IOException;
import java.util.function.Function;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public abstract class AANTLRLoader<K, P extends Parser, E, VI, VO> implements ILoader<K, VO> {
	protected abstract VO complete(VI value);

	protected abstract Function<? super E, ? extends VI> createBuilder(VI defaultResult);

	protected abstract CharStream createCharStream(K key) throws IOException;

	protected VI createDefaultResult(K key) {
		return null;
	}

	protected abstract Lexer createLexer(CharStream charStream);

	protected abstract P createParser(CommonTokenStream tokenStream);

	protected abstract E extract(P parser);

	protected boolean isFailOnError() {
		return true;
	}

	@Override
	public VO load(K key, ANTLRErrorListener... errorListeners) {
		final CharStream charStream;
		try {
			charStream = createCharStream(key);
		} catch (IOException e) {
			throw new RuntimeIOException(String.format("Failed to open %1$s for parsing!", key), e);
		}

		final Function<? super E, ? extends VI> builder = createBuilder(createDefaultResult(key));

		final Lexer lexer = createLexer(charStream);
		final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		final P parser = createParser(tokenStream);

		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		for (final ANTLRErrorListener errorListener : errorListeners) {
			lexer.addErrorListener(errorListener);
			parser.addErrorListener(errorListener);
		}
		final CollectingErrorListener collectingErrorListener;
		if (isFailOnError()) {
			collectingErrorListener = new CollectingErrorListener();
			lexer.addErrorListener(collectingErrorListener);
			parser.addErrorListener(collectingErrorListener);
		} else collectingErrorListener = null;

		final E extracted = extract(parser);
		if ((collectingErrorListener != null) && collectingErrorListener.hasErrors()) throw new RuntimeException("Parsing errors:\n" + collectingErrorListener.toString());
		final VI value = builder.apply(extracted);

		return complete(value);
	}
}
