package com.g2forge.enigma.frontend.antlr;

import java.util.function.Function;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HANTLR {
	public static <P extends Parser, Tree, T> T parse(final CharStream charStream, final Function<? super CharStream, ? extends Lexer> createLexer, final Function<? super CommonTokenStream, ? extends P> createParser, final Function<? super P, ? extends Tree> parse, final Function<? super Tree, ? extends T> build, final ANTLRErrorListener... errorListeners) {
		final Lexer lexer = createLexer.apply(charStream);
		final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		final P parser = createParser.apply(tokenStream);

		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		for (final ANTLRErrorListener errorListener : errorListeners) {
			lexer.addErrorListener(errorListener);
			parser.addErrorListener(errorListener);
		}
		final CollectingErrorListener checkingListener = new CollectingErrorListener();
		lexer.addErrorListener(checkingListener);
		parser.addErrorListener(checkingListener);

		final Tree tree = parse.apply(parser);
		if (checkingListener.hasErrors()) throw new RuntimeException("Parsing errors:\n" + checkingListener.toString());
		return build.apply(tree);
	}
}
