package com.cognizant.service;

import java.util.List;

import com.cognizant.dto.FineDTO;
import com.cognizant.dto.FineWithMember;

public interface FineService {
	List<FineDTO> getFinesByMember(Long memberId);
	FineDTO payFine(Long fineId);
	List<FineDTO> calculateFines();// Scheduled or triggered method
	List<FineWithMember> getAllFinesWithMemberDetails();
}