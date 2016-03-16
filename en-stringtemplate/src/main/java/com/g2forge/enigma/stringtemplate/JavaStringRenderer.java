package com.g2forge.enigma.stringtemplate;

import java.util.Locale;

import org.stringtemplate.v4.StringRenderer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JavaStringRenderer extends StringRenderer {
	protected final STGroupJava group;

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		if (!("java".equals(formatString))) return super.toString(o, formatString, locale);

		return group.render(o);
	}
}
