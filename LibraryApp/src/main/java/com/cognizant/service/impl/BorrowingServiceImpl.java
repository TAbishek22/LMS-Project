package com.cognizant.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cognizant.dto.BookDTO;
import com.cognizant.dto.BorrowingTransactionDTO;
import com.cognizant.dto.BorrowingTransactionWithBook;
import com.cognizant.dto.BorrowingWithMemberDTO;
import com.cognizant.dto.MemberDTO;
import com.cognizant.entity.Book;
import com.cognizant.entity.BorrowingTransaction;
import com.cognizant.entity.BorrowingTransaction.Status;
import com.cognizant.entity.MemberEntity;
import com.cognizant.exception.BookAlreadyReturnedException;
import com.cognizant.exception.BorrowingException;
import com.cognizant.exception.BorrowingLimitExceededException;
import com.cognizant.exception.TransactionNotFoundException;
import com.cognizant.repository.BookManagementModuleRepo;
import com.cognizant.repository.BorrowingTransactionRepository;
import com.cognizant.repository.MemberRepository;
import com.cognizant.service.BorrowingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class BorrowingServiceImpl implements BorrowingService {

	private static final Logger logger = LoggerFactory
			.getLogger(BorrowingServiceImpl.class);

	private final BorrowingTransactionRepository repository;
	private final BookManagementModuleRepo bookRepository;
	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;
	private static final int MAX_BORROW_LIMIT = 5;

	@Override
	public BorrowingTransactionDTO borrowBook(BorrowingTransactionDTO dto) {
		// Fetch book and validate existence
		Book book = bookRepository.findById(dto.getBookId())
				.orElseThrow(() -> new BorrowingException(
						"Book with ID " + dto.getBookId() + " not found"));

		// Fetch member and validate existence
		MemberEntity member = memberRepository.findById(dto.getMemberId())
				.orElseThrow(() -> new BorrowingException(
						"Member with ID " + dto.getMemberId() + " not found"));

		// Check available copies
		if (book.getAvailableCopies() <= 0) {
			throw new BorrowingException(
					"Book '" + book.getTitle() + "' is currently unavailable");
		}

		// Check if member has exceeded borrow limit
		long currentBorrowedCount = repository.countByMember_MemberIdAndStatus(
				member.getMemberId(), BorrowingTransaction.Status.BORROWED);
		if (currentBorrowedCount >= MAX_BORROW_LIMIT) {
			throw new BorrowingLimitExceededException(
					"Member has reached the maximum borrow limit of "
							+ MAX_BORROW_LIMIT + " books");
		}

		// Update book inventory
		book.setAvailableCopies(book.getAvailableCopies() - 1);
		bookRepository.save(book);

		// Set borrow and return dates
		LocalDate today = LocalDate.now();
		LocalDate returnDate = today.plusDays(14);

		// Create borrowing transaction
		BorrowingTransaction transaction = BorrowingTransaction.builder()
				.book(book).member(member).borrowDate(today)
				.returnDate(returnDate)
				.status(BorrowingTransaction.Status.BORROWED).build();

		BorrowingTransaction saved = repository.save(transaction);

		// Return DTO
		return new BorrowingTransactionDTO(saved.getTransactionId(),
				book.getBookId(), member.getMemberId(), saved.getBorrowDate(),
				saved.getReturnDate(), saved.getStatus());
	}
	@Override
	public BorrowingTransactionDTO returnBook(Long transactionId) {
		logger.info("Initiating return for transactionId: {}", transactionId);

		// Fetch the transaction
		BorrowingTransaction transaction = repository.findById(transactionId)
				.orElseThrow(() -> {
					logger.error("Transaction not found: {}", transactionId);
					return new TransactionNotFoundException(
							"Transaction with ID " + transactionId
									+ " not found.");
				});

		// Check if already returned
		if (transaction.getStatus() == Status.RETURNED) {
			logger.warn(
					"Attempt to return already returned book. Transaction ID: {}",
					transactionId);
			throw new BookAlreadyReturnedException(
					"Book has already been returned for transaction ID "
							+ transactionId);
		}

		// Update transaction status and return date
		transaction.setReturnDate(LocalDate.now());
		transaction.setStatus(Status.RETURNED);
		repository.save(transaction);

		// Increase book's available copies
		Book book = transaction.getBook();
		book.setAvailableCopies(book.getAvailableCopies() + 1);
		bookRepository.save(book);

		logger.info("Return successful for transaction ID: {}", transactionId);

		return modelMapper.map(transaction, BorrowingTransactionDTO.class);
	}

	@Override
	public List<BorrowingTransactionDTO> getBorrowedBooksByMember(
			Long memberId) {
		logger.info("Fetching borrowed books for memberId: {}", memberId);

		List<BorrowingTransactionDTO> transactions = repository
				.findByMember_MemberIdAndStatus(memberId, Status.BORROWED)
				.stream()
				.map(tr -> modelMapper.map(tr, BorrowingTransactionDTO.class))
				.collect(Collectors.toList());

		logger.debug("Found {} borrowed books for memberId: {}",
				transactions.size(), memberId);
		return transactions;
	}
	@Override
	public List<BorrowingTransactionDTO> getAllActiveBorrowings() {
		return repository.findByStatus(BorrowingTransaction.Status.BORROWED)
				.stream()
				.map(tr -> modelMapper.map(tr, BorrowingTransactionDTO.class))
				.collect(Collectors.toList());
	}
	@Override
	public List<BorrowingTransactionWithBook> getAllBorrowingTransactionsWithBookDetails() {
		List<BorrowingTransaction> transactions = repository.findAll();

		return transactions.stream().map(tx -> {
			BorrowingTransactionDTO txDTO = modelMapper.map(tx,
					BorrowingTransactionDTO.class);

			// Directly access Book entity from the relationship
			BookDTO bookDTO = modelMapper.map(tx.getBook(), BookDTO.class);

			BorrowingTransactionWithBook combined = new BorrowingTransactionWithBook();
			combined.setBorrowingTransaction(txDTO);
			combined.setBook(bookDTO);
			return combined;
		}).collect(Collectors.toList());
	}

	@Override
	public List<BorrowingWithMemberDTO> getAllBorrowingsWithMemberDetails() {
		List<BorrowingTransaction> transactions = repository.findAll();

		return transactions.stream().map(tx -> {
			BorrowingTransactionDTO txDTO = modelMapper.map(tx,
					BorrowingTransactionDTO.class);

			// Directly access Member entity from the relationship
			MemberDTO memberDTO = modelMapper.map(tx.getMember(),
					MemberDTO.class);

			return BorrowingWithMemberDTO.builder().member(memberDTO)
					.borrowingTransaction(txDTO).build();
		}).collect(Collectors.toList());
	}

}