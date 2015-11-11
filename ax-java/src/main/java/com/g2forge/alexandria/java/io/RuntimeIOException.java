package com.g2forge.alexandria.java.io;

public class RuntimeIOException extends RuntimeException {
	private static final long serialVersionUID = -2918473990606425790L;

	public RuntimeIOException() {}

	public RuntimeIOException(String message) {
		super(message);
	}

	public RuntimeIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RuntimeIOException(Throwable cause) {
		super(cause);
	}
}
