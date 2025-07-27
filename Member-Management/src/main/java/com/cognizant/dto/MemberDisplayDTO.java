package com.cognizant.dto;
 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor

public class MemberDisplayDTO {
    @Positive(message = "Member ID must be a positive number")
    private Long memberId;
 
    @NotEmpty(message = "Name cannot be empty")
    private String name;
 
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
 
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private String phone;
 
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    @NotBlank(message = "Address cannot be empty")
    private String address;
    
    
    @Size(min =6, message="Password must be at least 6 characters")
    private String password;
 
    
    @Pattern(regexp = "(Active|Inactive|Pending)", message = "Membership status must be Active, Inactive, or Pending")
    @NotBlank(message = "Membership status cannot be empty")
    private String membershipStatus;
 
    
//    public MemberDisplayDTO(Long memberId, String name, String email, String phone, String address,String password, String membershipStatus) {
//        this.memberId = memberId;
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//        this.address = address;
//        this.password = password;
//        this.membershipStatus = membershipStatus;
//    }

}
 
 