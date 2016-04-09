package org.stringtemplate.v4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STWriter;
import org.stringtemplate.v4.misc.ErrorManager;

public class STAdvanced extends ST {
	protected final String lineSeparator;

	public STAdvanced(ST proto, String lineSeparator) {
		super(proto);
		this.lineSeparator = lineSeparator;
	}

	protected STWriter createAutoIndentWriter(int lineWidth, final Writer out) {
		final STWriter retVal = new AutoIndentWriter(out, lineSeparator);
		retVal.setLineWidth(lineWidth);
		return retVal;
	}

	protected Interpreter createInterpreter(Locale locale, ErrorManager errorManager) {
		return new AdvancedInterpreter(groupThatCreatedThisInstance, locale, errorManager, false);
	}

	public Map<String, Object> getAttributes() {
		final Map<String, Object> retVal = super.getAttributes();
		if (retVal != null) retVal.replaceAll((name, value) -> STAttributeGenerator.unwrap(value));
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

	public int write(STWriter out, Locale locale) {
		return createInterpreter(locale, impl.nativeGroup.errMgr).exec(out, this);
	}

	public int write(STWriter out, Locale locale, STErrorListener listener) {
		return createInterpreter(locale, new ErrorManager(listener)).exec(out, this);
	}
}