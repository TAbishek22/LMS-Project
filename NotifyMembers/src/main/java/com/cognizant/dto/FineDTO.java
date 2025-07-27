package com.cognizant.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FineDTO {

    @NotNull(message = "Fine ID cannot be null")
    @Positive(message = "Fine ID must be a positive number")
    private Long fineId;

    @NotNull(message = "Member ID cannot be null")
    @Positive(message = "Member ID must be a positive number")
    private Long memberId;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 1, message = "Fine amount must be at least 1")
    private double amount;

    @NotNull(message = "Status cannot be null")
    private String status;

    @NotNull(message = "Transaction date cannot be null")
    private LocalDate transactionDate;
}
