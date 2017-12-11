package com.g2forge.enigma.frontend;

import java.io.PrintStream;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrintingErrorListener extends BaseErrorListener {
	protected final PrintStream out;

	@Override
	public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int character, final String message, final RecognitionException exception) {
		final String source = recognizer.getInputStream().getSourceName();
		out.println(String.format("%s:%d:%d: %s", source, line, character, message));
	}
}