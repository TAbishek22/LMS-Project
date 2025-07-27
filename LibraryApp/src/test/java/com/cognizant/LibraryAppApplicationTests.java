package com.cognizant;


import com.cognizant.dto.*;
import com.cognizant.entity.*;
import com.cognizant.exception.*;
import com.cognizant.repository.*;
import com.cognizant.service.impl.BorrowingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LibraryAppApplicationTests {

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @Mock
    private BorrowingTransactionRepository repository;

    @Mock
    private BookManagementModuleRepo bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        borrowingService = new BorrowingServiceImpl(repository, bookRepository, memberRepository, modelMapper);
    }

    @Test
    public void testBorrowBook_Success() {
        BorrowingTransactionDTO dto = new BorrowingTransactionDTO(null, 1L, 1L, null, null, null);
        Book book = Book.builder().bookId(1L).title("Java").availableCopies(2).build();
        MemberEntity member = MemberEntity.builder().memberId(1L).build();
        BorrowingTransaction savedTransaction = BorrowingTransaction.builder()
                .transactionId(10L).book(book).member(member)
                .borrowDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(14))
                .status(BorrowingTransaction.Status.BORROWED).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(repository.countByMember_MemberIdAndStatus(1L, BorrowingTransaction.Status.BORROWED)).thenReturn(2L);
        when(repository.save(any(BorrowingTransaction.class))).thenReturn(savedTransaction);

        BorrowingTransactionDTO result = borrowingService.borrowBook(dto);

        assertEquals(10L, result.getTransactionId());
        verify(bookRepository, times(1)).save(book);
        verify(repository, times(1)).save(any(BorrowingTransaction.class));
    }

    @Test
    public void testBorrowBook_ThrowsBookUnavailableException() {
        BorrowingTransactionDTO dto = new BorrowingTransactionDTO(null, 1L, 1L, null, null, null);
        Book book = Book.builder().bookId(1L).availableCopies(0).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BorrowingException.class, () -> borrowingService.borrowBook(dto));
    }

    @Test
    public void testReturnBook_Success() {
        Long transactionId = 1L;
        Book book = Book.builder().bookId(1L).availableCopies(2).build();
        BorrowingTransaction tx = BorrowingTransaction.builder()
                .transactionId(transactionId)
                .status(BorrowingTransaction.Status.BORROWED)
                .book(book)
                .build();
        BorrowingTransactionDTO txDto = new BorrowingTransactionDTO();

        when(repository.findById(transactionId)).thenReturn(Optional.of(tx));
        when(repository.save(any())).thenReturn(tx);
        when(bookRepository.save(any())).thenReturn(book);
        when(modelMapper.map(any(), eq(BorrowingTransactionDTO.class))).thenReturn(txDto);

        BorrowingTransactionDTO result = borrowingService.returnBook(transactionId);

        assertNotNull(result);
        verify(repository, times(1)).save(tx);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testReturnBook_AlreadyReturned_ThrowsException() {
        BorrowingTransaction tx = BorrowingTransaction.builder()
                .transactionId(1L)
                .status(BorrowingTransaction.Status.RETURNED)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(tx));

        assertThrows(BookAlreadyReturnedException.class, () -> borrowingService.returnBook(1L));
    }

    @Test
    public void testGetBorrowedBooksByMember_ReturnsList() {
        BorrowingTransaction tx = BorrowingTransaction.builder().build();
        BorrowingTransactionDTO txDto = new BorrowingTransactionDTO();

        when(repository.findByMember_MemberIdAndStatus(anyLong(), eq(BorrowingTransaction.Status.BORROWED)))
                .thenReturn(List.of(tx));
        when(modelMapper.map(tx, BorrowingTransactionDTO.class)).thenReturn(txDto);

        List<BorrowingTransactionDTO> result = borrowingService.getBorrowedBooksByMember(1L);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllActiveBorrowings_ReturnsList() {
        BorrowingTransaction tx = BorrowingTransaction.builder().build();
        BorrowingTransactionDTO txDto = new BorrowingTransactionDTO();

        when(repository.findByStatus(BorrowingTransaction.Status.BORROWED))
                .thenReturn(List.of(tx));
        when(modelMapper.map(tx, BorrowingTransactionDTO.class)).thenReturn(txDto);

        List<BorrowingTransactionDTO> result = borrowingService.getAllActiveBorrowings();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBorrowingTransactionsWithBookDetails() {
        Book book = Book.builder().title("Test Book").build();
        BorrowingTransaction tx = BorrowingTransaction.builder().book(book).build();
        BorrowingTransactionDTO txDto = new BorrowingTransactionDTO();
        BookDTO bookDto = new BookDTO();
        
        when(repository.findAll()).thenReturn(List.of(tx));
        when(modelMapper.map(tx, BorrowingTransactionDTO.class)).thenReturn(txDto);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDto);

        List<BorrowingTransactionWithBook> result = borrowingService.getAllBorrowingTransactionsWithBookDetails();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBorrowingsWithMemberDetails() {
        MemberEntity member = MemberEntity.builder().memberId(1L).build();
        BorrowingTransaction tx = BorrowingTransaction.builder().member(member).build();
        BorrowingTransactionDTO txDto = new BorrowingTransactionDTO();
        MemberDTO memberDto = new MemberDTO();

        when(repository.findAll()).thenReturn(List.of(tx));
        when(modelMapper.map(tx, BorrowingTransactionDTO.class)).thenReturn(txDto);
        when(modelMapper.map(member, MemberDTO.class)).thenReturn(memberDto);

        List<BorrowingWithMemberDTO> result = borrowingService.getAllBorrowingsWithMemberDetails();

        assertEquals(1, result.size());
    }
}

