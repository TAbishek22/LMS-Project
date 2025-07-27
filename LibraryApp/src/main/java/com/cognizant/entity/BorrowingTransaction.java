package com.cognizant.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "borrowing_transactions")
public class BorrowingTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	private LocalDate borrowDate;
	private LocalDate returnDate;

	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
		BORROWED, RETURNED
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;
	
	
	
	public BorrowingTransaction(Long transactionId, Book book, MemberEntity member, LocalDate borrowDate, LocalDate returnDate, BorrowingTransaction.Status status) {
	    this.transactionId = transactionId;
	    this.book = book;
	    this.member = member;
	    this.borrowDate = borrowDate;
	    this.returnDate = returnDate;
	    this.status = status;
	}

}
