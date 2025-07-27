package com.cognizant.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.dto.MemberDTO;

@FeignClient(name = "Member-Management")
public interface MemberClient {

	@GetMapping("/api/members/{memberId}")
	MemberDTO getMemberById(@PathVariable Long memberId);
}