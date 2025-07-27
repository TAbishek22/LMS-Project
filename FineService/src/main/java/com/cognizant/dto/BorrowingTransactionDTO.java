package com.cognizant.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BorrowingTransactionDTO {

    @NotNull(message = "Transaction ID cannot be null")
    @Positive(message = "Transaction ID must be a positive number")
    private Long transactionId;

    @NotNull(message = "Book ID cannot be null")
    @Positive(message = "Book ID must be a positive number")
    private Long bookId;

    @NotNull(message = "Member ID cannot be null")
    @Positive(message = "Member ID must be a positive number")
    private Long memberId;

    @NotNull(message = "Borrow date cannot be null")
    @FutureOrPresent(message = "Borrow date must be today or in the future")
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @NotNull(message = "Status cannot be null")
    private String status;
}
