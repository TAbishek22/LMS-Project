package com.cognizant.config;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.cognizant.dto.BorrowingTransactionDTO;

@FeignClient(name = "LibraryApp") // Change port
																	// if needed
public interface BorrowingClient {

	@GetMapping("/api/borrowings/all")
	List<BorrowingTransactionDTO> getAllBorrowedTransactions();
}
