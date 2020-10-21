package com.epam.esm.controller.exception;

public class NotSavedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotSavedException() {
	}

	public NotSavedException(String message) {
		super(message);
	}

	public NotSavedException(Throwable cause) {
		super(cause);
	}

	public NotSavedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotSavedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
