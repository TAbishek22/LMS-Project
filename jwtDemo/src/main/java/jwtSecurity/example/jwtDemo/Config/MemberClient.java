package jwtSecurity.example.jwtDemo.Config;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jwtSecurity.example.jwtDemo.Dto.MemberDTO;
import jwtSecurity.example.jwtDemo.Dto.MemberDisplayDTO;

@FeignClient(name = "member-service", url = "http://localhost:8082") // replace with actual service name or Eureka ID
public interface MemberClient {

    @PostMapping("/api/members/login")
    MemberDTO loginMember(@RequestParam String email, @RequestParam String password);
    
    @PostMapping("/api/members")
    MemberDisplayDTO registerMember(@RequestBody MemberDisplayDTO memberDTO);
 
  
}

