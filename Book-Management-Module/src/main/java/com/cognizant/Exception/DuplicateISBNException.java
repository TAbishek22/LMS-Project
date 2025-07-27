package com.cognizant.Exception;
 
public class DuplicateISBNException extends RuntimeException{
	public DuplicateISBNException(String msg) {
		super(msg);
	}
}