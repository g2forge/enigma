package com.g2forge.alexandria.java.enums;

import com.g2forge.alexandria.java.CaseException;

public class EnumException extends CaseException {
	private static final long serialVersionUID = -6025158954060585788L;

	public <E extends Enum<E>> EnumException(Class<E> klass, E value) {
		super("Value \"" + value + "\" from enum \"" + klass + "\" was not recognized!");
	}
}
