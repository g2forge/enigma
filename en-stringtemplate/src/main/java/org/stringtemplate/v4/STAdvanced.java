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

/**
 * Adds support for the advanced interpreter and custom line separators.
 */
public class STAdvanced extends ST {
	protected final String lineSeparator;

	public STAdvanced(ST proto, String lineSeparator) {
		super(proto);
		this.lineSeparator = lineSeparator;
	}

	/**
	 * Helper method to create an {@link AutoIndentWriter} with the correct line separator.
	 * 
	 * @param lineWidth
	 * @param out
	 * @return
	 */
	protected STWriter createAutoIndentWriter(int lineWidth, final Writer out) {
		final STWriter retVal = new AutoIndentWriter(out, lineSeparator);
		retVal.setLineWidth(lineWidth);
		return retVal;
	}

	/**
	 * Helper method to create an advanced interpreter.
	 * 
	 * @param locale
	 * @param errorManager
	 * @return
	 */
	protected Interpreter createInterpreter(Locale locale, ErrorManager errorManager) {
		return new AdvancedInterpreter(groupThatCreatedThisInstance, locale, errorManager, false);
	}

	/**
	 * Override to support {@link STAttributeGenerator}.
	 */
	@Override
	public Map<String, Object> getAttributes() {
		final Map<String, Object> retVal = super.getAttributes();
		if (retVal != null) retVal.replaceAll((name, value) -> STAttributeGenerator.unwrap(value));
		return retVal;
	}

	/**
	 * Override to support custom line separators.
	 */
	@Override
	public String render(Locale locale, int lineWidth) {
		final StringWriter retVal = new StringWriter();
		write(createAutoIndentWriter(lineWidth, retVal), locale);
		return retVal.toString();
	}

	/**
	 * Override to support custom line separators.
	 */
	@Override
	public int write(File outputFile, STErrorListener listener, String encoding, Locale locale, int lineWidth) throws IOException {
		try (final Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), encoding))) {
			return write(createAutoIndentWriter(lineWidth, bufferedWriter), locale, listener);
		}
	}

	/**
	 * Override to support advanced interpreter.
	 */
	@Override
	public int write(STWriter out, Locale locale) {
		final InstanceScope scope = new InstanceScope(null, this);
		return createInterpreter(locale, impl.nativeGroup.errMgr).exec(out, scope);
	}

	/**
	 * Override to support advanced interpreter.
	 */
	@Override
	public int write(STWriter out, Locale locale, STErrorListener listener) {
		final InstanceScope scope = new InstanceScope(null, this);
		return createInterpreter(locale, new ErrorManager(listener)).exec(out, scope);
	}
}