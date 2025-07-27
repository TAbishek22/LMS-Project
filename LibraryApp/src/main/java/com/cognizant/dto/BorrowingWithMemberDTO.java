package com.cognizant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingWithMemberDTO {

    @NotNull(message = "Member details cannot be null")
    @Valid
    private MemberDTO member;

    @NotNull(message = "Borrowing transaction cannot be null")
    @Valid
    private BorrowingTransactionDTO borrowingTransaction;
}
