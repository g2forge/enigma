package com.g2forge.enigma.stringtemplate;

public class NoTemplateException extends IllegalArgumentException {
	private static final long serialVersionUID = 3796469189459032363L;

	protected NoTemplateException(String messag) {
		super(messag);
	}
}