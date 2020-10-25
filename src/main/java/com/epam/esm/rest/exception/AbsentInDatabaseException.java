package com.epam.esm.rest.exception;

public class AbsentInDatabaseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AbsentInDatabaseException() {
	}

	public AbsentInDatabaseException(String message) {
		super(message);
	}

	public AbsentInDatabaseException(Throwable cause) {
		super(cause);
	}

	public AbsentInDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbsentInDatabaseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
