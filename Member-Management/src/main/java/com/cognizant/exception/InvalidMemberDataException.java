package com.cognizant.exception;

public class InvalidMemberDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public InvalidMemberDataException(String message) {
        super(message);
    }
     public InvalidMemberDataException(String message, Throwable cause) {
        super(message, cause);
    }
}