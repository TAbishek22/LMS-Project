package com.cognizant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.entity.BorrowingTransaction;

public interface BorrowingTransactionRepository
		extends
			JpaRepository<BorrowingTransaction, Long> {
	List<BorrowingTransaction> findByMember_MemberIdAndStatus(Long memberId,
			BorrowingTransaction.Status status);
	Long countByMember_MemberIdAndStatus(Long memberId,
			BorrowingTransaction.Status status);
	List<BorrowingTransaction> findByStatus(BorrowingTransaction.Status status);
}
