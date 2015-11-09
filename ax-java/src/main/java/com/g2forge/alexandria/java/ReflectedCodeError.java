package com.g2forge.alexandria.java;

public class ReflectedCodeError extends Error {
	private static final long serialVersionUID = 3425710259874935301L;

	public ReflectedCodeError() {}

	public ReflectedCodeError(String message) {
		super(message);
	}

	public ReflectedCodeError(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectedCodeError(Throwable cause) {
		super(cause);
	}
}
