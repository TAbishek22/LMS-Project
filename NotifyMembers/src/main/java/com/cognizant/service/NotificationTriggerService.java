package com.cognizant.service;

import java.util.List;

import com.cognizant.dto.NotificationWithMemberDTO;

public interface NotificationTriggerService {
	void notifyDueSoon();
	void notifyOverdue();
	void notifyFine();
	List<NotificationWithMemberDTO> getAllNotificationsWithMemberDetails();

}
