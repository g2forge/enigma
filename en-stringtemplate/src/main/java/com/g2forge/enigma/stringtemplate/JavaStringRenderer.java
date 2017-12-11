package com.g2forge.enigma.stringtemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.DateRenderer;
import org.stringtemplate.v4.NumberRenderer;
import org.stringtemplate.v4.StringRenderer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JavaStringRenderer implements AttributeRenderer {
	protected final StringRenderer string = new StringRenderer();

	protected final NumberRenderer number = new NumberRenderer();

	protected final DateRenderer date = new DateRenderer();

	protected final STGroupJava group;

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		try {
			return group.render(o);
		} catch (NoTemplateException exception) {
			if (o instanceof String) return string.toString(o, formatString, locale);
			if (o instanceof Number) return number.toString(o, formatString, locale);
			if (o instanceof Date || o instanceof Calendar) return date.toString(o, formatString, locale);
			return o.toString();
		}
	}
}
