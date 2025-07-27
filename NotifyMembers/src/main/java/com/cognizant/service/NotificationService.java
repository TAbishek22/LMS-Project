package com.cognizant.service;

import java.util.List;

import com.cognizant.dto.NotificationDTO;

public interface NotificationService {
	void sendNotification(NotificationDTO dto);
	List<NotificationDTO> getNotificationsByMember(Long memberId);
}
