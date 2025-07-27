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
public class FineWithMember {

    @NotNull(message = "Fine details cannot be null")
    @Valid
    private FineDTO fine;

    @NotNull(message = "Member details cannot be null")
    @Valid
    private MemberDTO member;
}
