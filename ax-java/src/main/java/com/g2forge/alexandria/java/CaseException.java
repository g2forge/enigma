package com.g2forge.alexandria.java;

public class CaseException extends Error {
	private static final long serialVersionUID = -3726966027884107285L;

	public CaseException() {}

	public CaseException(String message) {
		super(message);
	}

	public CaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaseException(Throwable cause) {
		super(cause);
	}
}
