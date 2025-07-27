package com.cognizant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    @NotNull(message = "Book ID cannot be null")
    @Positive(message = "Book ID must be a positive number")
    private Long bookId;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    private String author;

    @NotBlank(message = "Genre cannot be blank")
    private String genre;

    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be a 13-digit number")
    private String isbn;

    @Min(value = 1450, message = "Year published must be after 1450")
    private int yearPublished;

    @Min(value = 0, message = "Available copies cannot be negative")
    private int availableCopies;
}
