package com.cognizant.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cognizant.config.BorrowingClient;
import com.cognizant.config.MemberClient;
import com.cognizant.dto.BorrowingTransactionDTO;
import com.cognizant.dto.FineDTO;
import com.cognizant.dto.FineWithMember;
import com.cognizant.dto.MemberDTO;
import com.cognizant.entity.Fine;
import com.cognizant.exception.ResourceNotFoundException;
import com.cognizant.repository.FineRepository;
import com.cognizant.service.FineService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FineServiceImpl implements FineService {

	private final FineRepository fineRepository;
	private final BorrowingClient borrowingClient;
	private final ModelMapper modelMapper;
	private final MemberClient memberClient;
	private static final double FIRST_DAY_FINE = 10.0;

	@Override
	public List<FineDTO> calculateFines() {
		List<BorrowingTransactionDTO> borrowedBooks = borrowingClient
				.getAllBorrowedBooks();
		List<FineDTO> generatedFines = new ArrayList<>();

		for (BorrowingTransactionDTO tx : borrowedBooks) {
			if (tx.getReturnDate().isBefore(LocalDate.now())) {
				long overdueDays = LocalDate.now().toEpochDay()
						- tx.getReturnDate().toEpochDay();

				double fineAmount;
				if (overdueDays == 1) {
					fineAmount = FIRST_DAY_FINE;
				} else {
					fineAmount = FIRST_DAY_FINE * Math.pow(2, overdueDays - 1);
				}

				Fine fine = Fine.builder().memberId(tx.getMemberId())
						.amount(fineAmount).status(Fine.Status.PENDING)
						.transactionDate(LocalDate.now()).build();

				Fine savedFine = fineRepository.save(fine);
				generatedFines.add(modelMapper.map(savedFine, FineDTO.class));
				log.info("Fine calculated for memberId {}: Rs.{}",
						tx.getMemberId(), fineAmount);
			}
		}

		return generatedFines;
	}

	@Override
	public List<FineDTO> getFinesByMember(Long memberId) {
		return fineRepository.findByMemberId(memberId).stream()
				.map(fine -> modelMapper.map(fine, FineDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public FineDTO payFine(Long fineId) {
	    Fine fine = fineRepository.findById(fineId)
	            .orElseThrow(() -> new ResourceNotFoundException("Fine with ID " + fineId + " not found"));

	    fine.setStatus(Fine.Status.PAID);
	    fineRepository.save(fine);

	    return modelMapper.map(fine, FineDTO.class);
	}

	@Override
	public List<FineWithMember> getAllFinesWithMemberDetails() {
		List<Fine> fines = fineRepository.findAll();

		return fines.stream().map(fine -> {
			FineDTO fineDTO = modelMapper.map(fine, FineDTO.class);
			MemberDTO memberDTO = memberClient
					.getMemberById(fine.getMemberId());

			FineWithMember response = FineWithMember.builder().fine(fineDTO)
					.member(memberDTO).build();
			return response;
		}).collect(Collectors.toList());
	}
}
