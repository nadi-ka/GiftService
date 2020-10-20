package com.epam.esm.dal.exception;

public class DataSourceNamingException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DataSourceNamingException() {
		
	}

	public DataSourceNamingException(String message) {
		super(message);
	}

	public DataSourceNamingException(Throwable cause) {
		super(cause);
	}

	public DataSourceNamingException(String message, Throwable cause) {
		super(message, cause);
	}

}
