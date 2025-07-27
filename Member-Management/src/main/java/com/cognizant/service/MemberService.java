package com.cognizant.service;

import java.util.List;

import com.cognizant.dto.MemberDTO;
import com.cognizant.dto.MemberDisplayDTO;

public interface MemberService {
    MemberDisplayDTO addMember(MemberDisplayDTO memberDTO);
    List<MemberDTO> getAllMembers();
    MemberDTO getMemberById(Long memberId);
    MemberDTO updateMember(Long memberId, MemberDTO memberDTO);
    void deleteMember(Long memberId);
    String getMemberEmail(Long memberId);
    MemberDTO getMemberByEmail(String email);
    MemberDTO getMemberByPhone(String phone);
    List<MemberDTO> getMembersByStatus(String status);
    List<MemberDTO> getMembersByName(String name);
	MemberDTO login(String email, String rawPassword);
     
     
}
