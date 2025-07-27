package com.cognizant.exception;

public class MemberPhoneAlreadyExistsException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public MemberPhoneAlreadyExistsException(String message) {
        super(message);
    }
}
