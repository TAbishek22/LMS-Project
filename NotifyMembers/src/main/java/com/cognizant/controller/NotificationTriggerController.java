package com.cognizant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.dto.NotificationWithMemberDTO;
import com.cognizant.service.NotificationTriggerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationTriggerController {

	private final NotificationTriggerService triggerService;

	@GetMapping("/test/due-soon")
	public ResponseEntity<String> testDueSoon() {
		triggerService.notifyDueSoon();
		return ResponseEntity.ok("Due Soon Notifications Sent");
	}

	@GetMapping("/test/overdue")
	public ResponseEntity<String> testOverdue() {
		triggerService.notifyOverdue();
		return ResponseEntity.ok("Overdue Notifications Sent");
	}
	@GetMapping("/test/fine")
	public ResponseEntity<String> testFine() {
		triggerService.notifyFine();
		return ResponseEntity.ok("Fine Notifications Sent");
	}
	@GetMapping("/all-with-members")
	public ResponseEntity<List<NotificationWithMemberDTO>> getAllWithMemberDetails() {
		return ResponseEntity
				.ok(triggerService.getAllNotificationsWithMemberDetails());
	}
}
