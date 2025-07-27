package com.cognizant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	private Long bookId;
	@NotBlank(message = "Title is required")
	private String title;
	@NotBlank(message = "Author is required")
	private String author;
	private String genre;
	@Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "ISBN must be valid")
	private String isbn;
	@Min(value = 1000, message = "Year published must be vaild")
	private Integer yearPublished;
	@NotNull(message = "Available copies count is required")
	@Min(value = 0, message = "Available copies cannot be negative")
	private Integer availableCopies;
}
