package com.cognizant.config;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.cognizant.dto.FineDTO;

@FeignClient(name = "FineService")
public interface FineClient {
	@PostMapping("/api/fines/calculate")
	List<FineDTO> calculateFines();
}
