package com.cognizant;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cognizant.config.BorrowingClient;
import com.cognizant.config.FineClient;
import com.cognizant.config.MemberClient;
import com.cognizant.dto.BorrowingTransactionDTO;
import com.cognizant.dto.FineDTO;
import com.cognizant.dto.MemberDTO;
import com.cognizant.dto.NotificationDTO;
import com.cognizant.entity.Notification;
import com.cognizant.repository.NotificationRepository;
import com.cognizant.service.NotificationService;
import com.cognizant.service.impl.NotificationTriggerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class NotifyMembersApplicationTests {

    @Mock
    private BorrowingClient borrowingClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private FineClient fineClient;

    @Mock
    private MemberClient memberClient;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationTriggerServiceImpl notificationTriggerService;

    private BorrowingTransactionDTO borrowingTransactionDTO;
    private FineDTO fineDTO;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        borrowingTransactionDTO = new BorrowingTransactionDTO(1L, 1001L, 101L, LocalDate.now().minusDays(5), LocalDate.now().plusDays(2), "BORROWED");
        fineDTO = new FineDTO(1L, 101L, 50.0, "PENDING", LocalDate.now());
        memberDTO = new MemberDTO(101L, "John Doe", "johndoe@example.com", "9876543210", "123 Street", "ACTIVE");
    }

    @Test
    void testNotifyDueSoon() {
        when(borrowingClient.getAllBorrowedTransactions()).thenReturn(List.of(borrowingTransactionDTO));
        when(memberClient.getMemberById(101L)).thenReturn(memberDTO);

        notificationTriggerService.notifyDueSoon();

        verify(notificationService, times(1)).sendNotification(any(NotificationDTO.class));
    }

    @Test
    void testNotifyOverdue() {
        borrowingTransactionDTO.setReturnDate(LocalDate.now().minusDays(1));
        when(borrowingClient.getAllBorrowedTransactions()).thenReturn(List.of(borrowingTransactionDTO));
        when(memberClient.getMemberById(101L)).thenReturn(memberDTO);

        notificationTriggerService.notifyOverdue();

        verify(notificationService, times(1)).sendNotification(any(NotificationDTO.class));
    }

    @Test
    void testNotifyFine() {
        when(fineClient.calculateFines()).thenReturn(List.of(fineDTO));
        when(memberClient.getMemberById(101L)).thenReturn(memberDTO);

        notificationTriggerService.notifyFine();

        verify(notificationService, times(1)).sendNotification(any(NotificationDTO.class));
    }
}
