package com.g2forge.enigma.frontend;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import lombok.Getter;

public class CollectingErrorListener extends BaseErrorListener {
	@Getter
	protected final List<String> log = new ArrayList<>();

	public boolean hasErrors() {
		return !getLog().isEmpty();
	}

	public void print(final PrintStream out) {
		getLog().forEach(out::println);
	}

	@Override
	public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int character, final String message, final RecognitionException exception) {
		final String source = recognizer.getInputStream().getSourceName();
		getLog().add(String.format("%s:%d:%d: %s", source, line, character, message));
	}

	@Override
	public String toString() {
		return getLog().stream().collect(Collectors.joining("\n"));
	}
}