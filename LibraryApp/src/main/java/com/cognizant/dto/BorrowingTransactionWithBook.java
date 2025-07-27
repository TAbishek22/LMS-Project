package com.cognizant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingTransactionWithBook {

    @NotNull(message = "Borrowing transaction details cannot be null")
    @Valid
    private BorrowingTransactionDTO borrowingTransaction;

    @NotNull(message = "Book details cannot be null")
    @Valid
    private BookDTO book;
}
