package com.cognizant.service;

import java.util.List;

import com.cognizant.dto.BorrowingTransactionDTO;
import com.cognizant.dto.BorrowingTransactionWithBook;
import com.cognizant.dto.BorrowingWithMemberDTO;

public interface BorrowingService {
	BorrowingTransactionDTO borrowBook(BorrowingTransactionDTO dto);
	BorrowingTransactionDTO returnBook(Long transactionId);
	List<BorrowingTransactionDTO> getBorrowedBooksByMember(Long memberId);
	List<BorrowingTransactionDTO> getAllActiveBorrowings();
	List<BorrowingTransactionWithBook> getAllBorrowingTransactionsWithBookDetails();
	List<BorrowingWithMemberDTO> getAllBorrowingsWithMemberDetails();

}
