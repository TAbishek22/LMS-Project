package com.cognizant.exception;

public class MemberEmailAlreadyExistsException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public MemberEmailAlreadyExistsException(String message) {
        super(message);
    }
}