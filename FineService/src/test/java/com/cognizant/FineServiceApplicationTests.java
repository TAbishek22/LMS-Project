package com.cognizant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.cognizant.config.BorrowingClient;
import com.cognizant.config.MemberClient;
import com.cognizant.dto.BorrowingTransactionDTO;
import com.cognizant.dto.FineDTO;
import com.cognizant.dto.FineWithMember;
import com.cognizant.dto.MemberDTO;
import com.cognizant.entity.Fine;
import com.cognizant.exception.ResourceNotFoundException;
import com.cognizant.repository.FineRepository;
import com.cognizant.service.impl.FineServiceImpl;

@ExtendWith(MockitoExtension.class)
public class FineServiceApplicationTests {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private BorrowingClient borrowingClient;

    @Mock
    private MemberClient memberClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FineServiceImpl fineService;

    private Fine fine;
    private FineDTO fineDTO;
    private BorrowingTransactionDTO borrowingTransactionDTO;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        fine = new Fine(1L, 101L, 50.0, Fine.Status.PENDING, LocalDate.now());
        fineDTO = new FineDTO(1L, 101L, 50.0, Fine.Status.PENDING, LocalDate.now());
        borrowingTransactionDTO = new BorrowingTransactionDTO(1L, 1001L, 101L, LocalDate.now().minusDays(5), LocalDate.now().minusDays(2), "RETURNED");
        memberDTO = new MemberDTO(101L, "John Doe", "johndoe@example.com", "9876543210", "123 Street", "ACTIVE");
    }

    @Test
    void testCalculateFines() {
        when(borrowingClient.getAllBorrowedBooks()).thenReturn(List.of(borrowingTransactionDTO));
        when(fineRepository.save(any(Fine.class))).thenReturn(fine);
        when(modelMapper.map(any(Fine.class), eq(FineDTO.class))).thenReturn(fineDTO);

        List<FineDTO> result = fineService.calculateFines();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(fineRepository, times(1)).save(any(Fine.class));
    }

    @Test
    void testGetFinesByMember() {
        when(fineRepository.findByMemberId(101L)).thenReturn(List.of(fine));
        when(modelMapper.map(any(Fine.class), eq(FineDTO.class))).thenReturn(fineDTO);

        List<FineDTO> result = fineService.getFinesByMember(101L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(fineRepository, times(1)).findByMemberId(101L);
    }

    @Test
    void testPayFine() {
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
        when(modelMapper.map(any(Fine.class), eq(FineDTO.class))).thenReturn(fineDTO);

        FineDTO result = fineService.payFine(1L);

        assertNotNull(result);
        assertEquals(Fine.Status.PAID, fine.getStatus());
        verify(fineRepository, times(1)).save(fine);
    }

    @Test
    void testGetAllFinesWithMemberDetails() {
        when(fineRepository.findAll()).thenReturn(List.of(fine));
        when(modelMapper.map(any(Fine.class), eq(FineDTO.class))).thenReturn(fineDTO);
        when(memberClient.getMemberById(101L)).thenReturn(memberDTO);

        List<FineWithMember> result = fineService.getAllFinesWithMemberDetails();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getMember().getName());
        verify(memberClient, times(1)).getMemberById(101L);
    }
}
