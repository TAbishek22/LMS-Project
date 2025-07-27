package com.cognizant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationWithMemberDTO {

    @NotNull(message = "Member details cannot be null")
    @Valid
    private MemberDTO memberDTO;

    @NotNull(message = "Notification details cannot be null")
    @Valid
    private NotificationDTO notificationDTO;
}
