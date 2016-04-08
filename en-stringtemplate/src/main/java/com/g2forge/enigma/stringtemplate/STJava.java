package com.g2forge.enigma.stringtemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STWriter;

public class STJava extends ST {
	protected final String lineSeparator;

	public STJava(ST proto, String lineSeparator) {
		super(proto);
		this.lineSeparator = lineSeparator;
	}

	protected STWriter createAutoIndentWriter(int lineWidth, final Writer out) {
		final STWriter retVal = new AutoIndentWriter(out, lineSeparator);
		retVal.setLineWidth(lineWidth);
		return retVal;
	}

	public String render(Locale locale, int lineWidth) {
		final StringWriter retVal = new StringWriter();
		write(createAutoIndentWriter(lineWidth, retVal), locale);
		return retVal.toString();
	}

	public int write(File outputFile, STErrorListener listener, String encoding, Locale locale, int lineWidth) throws IOException {
		try (final Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), encoding))) {
			return write(createAutoIndentWriter(lineWidth, bufferedWriter), locale, listener);
		}
	}
}