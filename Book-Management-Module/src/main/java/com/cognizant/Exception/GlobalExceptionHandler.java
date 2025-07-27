package com.cognizant.Exception;
 
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException ex){
		ErrorResponse errorResponse = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateISBNException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateISBNException(DuplicateISBNException ex){
		ErrorResponse errorResponse = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach( (error) -> {
			String fileName =  ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fileName, errorMessage);
		});
		ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"validation failed",
				LocalDateTime.now(),
				errors
				);
		return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ErrorResponse {
		private int status;
	    private String message;
	    private LocalDateTime timestamp;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ValidationErrorResponse {
		 private int status;
		 private String message;
		 private LocalDateTime timestamp;
		 private Map<String, String> errors;
	}
}