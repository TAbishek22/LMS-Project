package com.cognizant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import com.cognizant.dto.BookDTO;
import com.cognizant.entity.Book;
import com.cognizant.repository.BookManagementModuleRepo;
import com.cognizant.service.BookManagementModuleService;

@ExtendWith(MockitoExtension.class)
public class BookManagementModuleTests {

    @Mock
    private BookManagementModuleRepo bookManagementModuleRepo;

    @InjectMocks
    private BookManagementModuleService bookManagementModuleService;

    private BookDTO bookDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO(1L, "Test Title", "Test Author", "Test Genre", "1234567890123", 2021, 10);
        book = new Book(1L, "Test Title", "Test Author", "Test Genre", "1234567890123", 2021, 10);
    }

    @Test
    void testAddBook() {
        when(bookManagementModuleRepo.save(book)).thenReturn(book);

        BookDTO result = bookManagementModuleService.addBook(bookDTO);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(bookManagementModuleRepo, times(1)).save(book);
    }

    @Test
    void testUpdateBook() {
        when(bookManagementModuleRepo.findById(1L)).thenReturn(Optional.of(book));
        when(bookManagementModuleRepo.save(book)).thenReturn(book);

        BookDTO result = bookManagementModuleService.updateBook(1L, bookDTO);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(bookManagementModuleRepo, times(1)).findById(1L);
        verify(bookManagementModuleRepo, times(1)).save(book);
    }

    @Test
    void testGetAllBooks() {
        when(bookManagementModuleRepo.findByAvailableCopiesGreaterThan(0))
                .thenReturn(Stream.of(book).collect(Collectors.toList()));

        List<BookDTO> result = bookManagementModuleService.getAllBooks();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(bookManagementModuleRepo, times(1)).findByAvailableCopiesGreaterThan(0);
    }

    @Test
    void testGetBookById() {
        when(bookManagementModuleRepo.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookManagementModuleService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(bookManagementModuleRepo, times(1)).findById(1L);
    }

    @Test
    void testDeleteBookById() {
        when(bookManagementModuleRepo.existsById(1L)).thenReturn(true);
        doNothing().when(bookManagementModuleRepo).deleteById(1L);

        bookManagementModuleService.deleteBookById(1L);

        verify(bookManagementModuleRepo, times(1)).deleteById(1L);
    }
}
