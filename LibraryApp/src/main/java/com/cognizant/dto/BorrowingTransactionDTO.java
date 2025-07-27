package com.cognizant.dto;

import java.time.LocalDate;

import com.cognizant.entity.BorrowingTransaction;
import com.cognizant.entity.BorrowingTransaction.Status;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class BorrowingTransactionDTO {

    private Long transactionId;

    @NotNull(message = "Book ID is required")
    @Positive(message = "Book ID must be a positive number")
    private Long bookId;

    @NotNull(message = "Member ID is required")
    @Positive(message = "Member ID must be a positive number")
    private Long memberId;

    @NotNull(message = "Borrow date is required")
    @FutureOrPresent(message = "Borrow date must be today or in the future")
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @NotNull(message = "Status is required")
    private Status status;
    


}
