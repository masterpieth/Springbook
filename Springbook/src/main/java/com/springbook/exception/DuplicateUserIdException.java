package com.springbook.exception;

@SuppressWarnings("serial")
public class DuplicateUserIdException extends RuntimeException{
	public DuplicateUserIdException(Throwable cause) {
		super(cause);
	}

	public DuplicateUserIdException() {
	}
}
