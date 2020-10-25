package com.epam.esm.rest.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.epam.esm.rest.exception.AbsentInDatabaseException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.exception.NotSavedException;
import com.epam.esm.service.exception.IllegalOperationServiceException;

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException (NotFoundException exc) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				exc.getMessage(),
				System.currentTimeMillis());
	
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException (IllegalOperationServiceException exc) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.METHOD_NOT_ALLOWED.value(),
				exc.getMessage(),
				System.currentTimeMillis());
	
		return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException (AbsentInDatabaseException exc) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.OK.value(),
				exc.getMessage(),
				System.currentTimeMillis());
	
		return new ResponseEntity<>(error, HttpStatus.OK);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException (NotSavedException exc) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				exc.getMessage(),
				System.currentTimeMillis());
	
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException (Exception exc) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				exc.getMessage(),
				System.currentTimeMillis());
	
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
