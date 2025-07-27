package com.cognizant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.dto.FineDTO;
import com.cognizant.dto.FineWithMember;
import com.cognizant.service.FineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@Validated
@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

	private final FineService fineService;

	@GetMapping("/member/{memberId}")
	public ResponseEntity<List<FineDTO>> getFinesByMember(
			@PathVariable Long memberId) {
		return ResponseEntity.ok(fineService.getFinesByMember(memberId));
	}

	@PostMapping("/pay/{fineId}")
	public ResponseEntity<FineDTO> payFine(@Valid @PathVariable Long fineId) {
		return ResponseEntity.ok(fineService.payFine(fineId));
	}

	@PostMapping("/calculate")
	public ResponseEntity<List<FineDTO>> calculateFines() {
		List<FineDTO> result = fineService.calculateFines();
		return ResponseEntity.ok(result);
	}
	@GetMapping("/with-member")
	public List<FineWithMember> getFinesWithMembers() {
		return fineService.getAllFinesWithMemberDetails();
	}
}
