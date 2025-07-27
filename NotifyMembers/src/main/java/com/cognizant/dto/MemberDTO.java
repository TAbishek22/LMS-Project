package com.cognizant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class MemberDTO {

    @NotNull(message = "Member ID cannot be null")
    @Positive(message = "Member ID must be a positive number")
    private Long memberId;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number")
    private String phone;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;

    @NotBlank(message = "Membership status cannot be blank")
    private String membershipStatus;
    
    public MemberDTO(Long memberId, String name, String email, String phone, String address, String membershipStatus) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipStatus = membershipStatus;
    }

}
