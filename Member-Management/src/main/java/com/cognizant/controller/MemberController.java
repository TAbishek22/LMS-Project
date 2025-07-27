package com.cognizant.controller;

import com.cognizant.dto.MemberDTO;
import com.cognizant.dto.MemberDisplayDTO;
import com.cognizant.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@Validated
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberDisplayDTO> addMember(@Valid @RequestBody MemberDisplayDTO memberDTO) {
        MemberDisplayDTO savedMemberDTO = memberService.addMember(memberDTO);
        return new ResponseEntity<>(savedMemberDTO, HttpStatus.CREATED);
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<MemberDTO> loginMember(@RequestParam String email, @RequestParam String password) {
        MemberDTO memberDTO = memberService.login(email, password);
        return ResponseEntity.ok(memberDTO);
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        List<MemberDTO> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.getMemberById(memberId);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberDTO memberDTO) {
        MemberDTO updatedMemberDTO = memberService.updateMember(memberId, memberDTO);
        return new ResponseEntity<>(updatedMemberDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberDTO> getMemberByEmail(@PathVariable String email) {
        MemberDTO memberDTO = memberService.getMemberByEmail(email);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<MemberDTO> getMemberByPhone(@PathVariable String phone) {
        MemberDTO memberDTO = memberService.getMemberByPhone(phone);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MemberDTO>> getMembersByStatus(@PathVariable String status) {
        List<MemberDTO> members = memberService.getMembersByStatus(status);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
     public ResponseEntity<List<MemberDTO>> getMembersByName(@PathVariable String name) {
        List<MemberDTO> members = memberService.getMembersByName(name);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }
    @GetMapping("/{memberId}/email")
    public ResponseEntity<String> getMemberEmail(@PathVariable Long memberId) {
        String email = memberService.getMemberEmail(memberId);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }
}
