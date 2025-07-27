package com.cognizant.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cognizant.config.BorrowingClient;
import com.cognizant.config.FineClient;
import com.cognizant.config.MemberClient;
import com.cognizant.dto.BorrowingTransactionDTO;
import com.cognizant.dto.FineDTO;
import com.cognizant.dto.MemberDTO;
import com.cognizant.dto.NotificationDTO;
import com.cognizant.dto.NotificationWithMemberDTO;
import com.cognizant.entity.Notification;
import com.cognizant.repository.NotificationRepository;
import com.cognizant.service.NotificationService;
import com.cognizant.service.NotificationTriggerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationTriggerServiceImpl
		implements
			NotificationTriggerService {

	private final BorrowingClient borrowingClient;
	private final NotificationService notificationService;
	private final FineClient fineClient;
	private final MemberClient memberClient;
	private final NotificationRepository notificationRepository;

	@Override
	public void notifyDueSoon() {
		List<BorrowingTransactionDTO> transactions = borrowingClient
				.getAllBorrowedTransactions();

		LocalDate today = LocalDate.now();
		LocalDate threeDaysLater = today.plusDays(3);

		transactions.stream()
				.filter(txn -> txn.getStatus().equalsIgnoreCase("BORROWED"))
				.filter(txn -> txn.getReturnDate() != null)
				.filter(txn -> !txn.getReturnDate().isBefore(today)
						&& !txn.getReturnDate().isAfter(threeDaysLater))
				.forEach(txn -> {
					NotificationDTO dto = NotificationDTO.builder()
							.memberId(txn.getMemberId())
							.email(getEmailForMember(txn.getMemberId())) // implement
																			// accordingly
							.message("Dear " + getMemberName(txn.getMemberId()) + ",\n\n"
							        + "This is a friendly reminder that your borrowed book is due on **" + txn.getReturnDate() + "**.\n"
							        + "Please ensure you return it on time to avoid late fees.\n\n")
							.build();
					notificationService.sendNotification(dto);
				});

		log.info("Due Soon notifications sent.");
	}

	@Override
	public void notifyOverdue() {
		List<BorrowingTransactionDTO> transactions = borrowingClient
				.getAllBorrowedTransactions();

		LocalDate today = LocalDate.now();

		transactions.stream()
				.filter(txn -> txn.getStatus().equalsIgnoreCase("BORROWED"))
				.filter(txn -> txn.getReturnDate() != null
						&& txn.getReturnDate().isBefore(today))
				.forEach(txn -> {
					NotificationDTO dto = NotificationDTO.builder()
							.memberId(txn.getMemberId())
							.email(getEmailForMember(txn.getMemberId()))
							.message("Dear " + getMemberName(txn.getMemberId()) + ",\n\n"
							        + "Alert: Your borrowed book was due on **" + txn.getReturnDate() + "**.\n"
							        + "Please return it immediately to avoid late fines and restrictions on future borrowings.\n\n"
							       )
							.build();
					notificationService.sendNotification(dto);
				});

		log.info("Overdue notifications sent.");
	}
	@Override
	public void notifyFine() {
		List<FineDTO> fines = fineClient.calculateFines();

		fines.forEach(fine -> {
			NotificationDTO dto = NotificationDTO.builder()
					.memberId(fine.getMemberId())
					.email(getEmailForMember(fine.getMemberId()))
					.message("Dear " + getMemberName(fine.getMemberId()) + ",\n\n"
					        + "Important Notice: You have been fined **Rs. " + fine.getAmount() + "** due to overdue borrowed books.\n"
					        + "Please settle your pending fine at the earliest to avoid additional penalties or restrictions on future borrowings.\n\n")

					.build();

			notificationService.sendNotification(dto);
		});

		log.info("Fine notifications sent to {} members.", fines.size());
	}

	// This should ideally come from Member Service which is to be implemented
	private String getEmailForMember(Long memberId) {
		try {
			MemberDTO member = memberClient.getMemberById(memberId);
			if (member != null && member.getEmail() != null) {
				return member.getEmail();
			} else {
				log.warn("Could not retrieve email for member ID: {}",
						memberId);
				return null; // Or handle this case as needed (e.g., throw an
								// exception)
			}
		} catch (Exception e) {
			log.error("Error while fetching member details for ID {}: {}",
					memberId, e.getMessage());
			return null; // Or handle this exception
		}
	}
	private String getMemberName(Long memberId) {
	    try {
	        MemberDTO member = memberClient.getMemberById(memberId);
	        if (member != null && member.getName() != null) {
	            return member.getName();
	        } else {
	            log.warn("Could not retrieve name for member ID: {}", memberId);
	            return "Member"; // Default fallback name or handle differently if needed
	        }
	    } catch (Exception e) {
	        log.error("Error while fetching member details for ID {}: {}", memberId, e.getMessage());
	        return "Member"; // Default fallback name or handle exception accordingly
	    }
	}

	@Override
	public List<NotificationWithMemberDTO> getAllNotificationsWithMemberDetails() {
		List<Notification> notifications = notificationRepository.findAll();

		return notifications.stream().map(notification -> {
			MemberDTO member = memberClient
					.getMemberById(notification.getMemberId());

			NotificationDTO notificationDTO = NotificationDTO.builder()
					.memberId(notification.getMemberId())
					.email(notification.getEmail())
					.message(notification.getMessage()).build();

			return NotificationWithMemberDTO.builder().memberDTO(member)
					.notificationDTO(notificationDTO).build();
		}).collect(Collectors.toList());
	}
}
