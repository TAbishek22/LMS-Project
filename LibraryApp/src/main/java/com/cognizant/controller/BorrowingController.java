package com.cognizant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.dto.BorrowingTransactionDTO;
import com.cognizant.dto.BorrowingTransactionWithBook;
import com.cognizant.dto.BorrowingWithMemberDTO;
import com.cognizant.service.BorrowingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

	private final BorrowingService borrowingService;

	@PostMapping("/borrow")
	public ResponseEntity<BorrowingTransactionDTO> borrowBook(
			@RequestBody @Valid BorrowingTransactionDTO dto) {
		return ResponseEntity.ok(borrowingService.borrowBook(dto));
	}

	@PutMapping("/return/{transactionId}")
	public ResponseEntity<BorrowingTransactionDTO> returnBook(
			@PathVariable Long transactionId) {
		return ResponseEntity.ok(borrowingService.returnBook(transactionId));
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<List<BorrowingTransactionDTO>> getBorrowedBooks(
			@PathVariable Long memberId) {
		return ResponseEntity
				.ok(borrowingService.getBorrowedBooksByMember(memberId));
	}
	@GetMapping("/all")
	public ResponseEntity<List<BorrowingTransactionDTO>> getAllActiveBorrowings() {
		return ResponseEntity.ok(borrowingService.getAllActiveBorrowings());
	}

	@GetMapping("/with-books")
	public ResponseEntity<List<BorrowingTransactionWithBook>> getAllWithBooks() {
		return ResponseEntity.ok(
				borrowingService.getAllBorrowingTransactionsWithBookDetails());
	}

	@GetMapping("/with-member")
	public ResponseEntity<List<BorrowingWithMemberDTO>> getBorrowingsWithMemberDetails() {
		return ResponseEntity
				.ok(borrowingService.getAllBorrowingsWithMemberDetails());
	}

}
