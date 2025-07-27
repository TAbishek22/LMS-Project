package com.cognizant.service.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.dto.MemberDTO;
import com.cognizant.dto.MemberDisplayDTO;
import com.cognizant.entity.MemberEntity;
import com.cognizant.exception.InvalidMemberDataException;
import com.cognizant.exception.MemberEmailAlreadyExistsException;
import com.cognizant.exception.MemberNotFoundException;
import com.cognizant.repository.MemberRepository;
import com.cognizant.service.MemberService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);
    private BCryptPasswordEncoder passwordEncoder ;

   

    @Override
    public MemberDisplayDTO addMember(MemberDisplayDTO memberDTO) {
        LOGGER.info("Adding new member: {}", memberDTO);

        // Check if email already exists
        if (memberRepository.findByEmail(memberDTO.getEmail()).isPresent()) {
            LOGGER.warn("Member with email {} already exists!", memberDTO.getEmail());
            throw new MemberEmailAlreadyExistsException("Email already in use: " + memberDTO.getEmail());
        }

        // Encrypt password before saving
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));

        // Map DTO to Entity
        MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
        
        try {
            // Save Member
            MemberEntity savedMember = memberRepository.save(memberEntity);

            // Convert Entity back to DTO
            MemberDisplayDTO savedDto = modelMapper.map(savedMember, MemberDisplayDTO.class);
            LOGGER.info("Successfully added member with ID: {}", savedDto.getMemberId());

            return savedDto;
        } catch (Exception e) {
            LOGGER.error("Error adding member", e);
            throw new InvalidMemberDataException("Error adding member", e);
        }
    }
    
    @Override
    public MemberDTO login(String email, String rawPassword) {
        LOGGER.info("Authenticating member with email: {}", email);
        
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.warn("Login failed. Member with email {} not found.", email);
                    throw new MemberNotFoundException("Member with email " + email + " not found.");
                });

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            LOGGER.warn("Login failed. Password mismatch for email: {}", email);
            throw new MemberNotFoundException("Member with email " + email + ": Password is not Correct");
        }
         MemberDTO memberDto =  modelMapper.map(member,MemberDTO.class);

        LOGGER.info("Member authenticated successfully: {}", member.getMemberId());
        return memberDto;
    }


    @Override
    public List<MemberDTO> getAllMembers() {
        LOGGER.info("Retrieving all members");
        List<MemberEntity> memberEntities = memberRepository.findAll();
        return memberEntities.stream()
                .map(memberEntity -> modelMapper.map(memberEntity, MemberDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public String getMemberEmail(Long memberId) {
        LOGGER.info("Retrieving email for member ID: {}", memberId);
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));
        return memberEntity.getEmail();
    }

    @Override
    public MemberDTO getMemberById(Long memberId) {
        LOGGER.info("Retrieving member by ID: {}", memberId);
        Optional<MemberEntity> memberEntityOptional = memberRepository.findById(memberId);
        MemberEntity memberEntity = memberEntityOptional.orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));
        return modelMapper.map(memberEntity, MemberDTO.class);
    }

    @Override
    public MemberDTO updateMember(Long memberId, MemberDTO memberDTO) {
        LOGGER.info("Updating member with ID: {} and data: {}", memberId, memberDTO);
        Optional<MemberEntity> existingMemberOptional = memberRepository.findById(memberId);
        MemberEntity existingMember = existingMemberOptional.orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));

        modelMapper.map(memberDTO, existingMember); // Update existing member with DTO data
        existingMember.setMemberId(memberId); // Ensure ID is not changed
        MemberEntity updatedMember = memberRepository.save(existingMember);
        return modelMapper.map(updatedMember, MemberDTO.class);
    }

    @Override
    public void deleteMember(Long memberId) {
        LOGGER.info("Deleting member with ID: {}", memberId);
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("Member not found with ID: " + memberId);
        }
        memberRepository.deleteById(memberId);
    }

    @Override
    public MemberDTO getMemberByEmail(String email) {
         LOGGER.info("Retrieving member by email: {}", email);
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByEmail(email);
        MemberEntity memberEntity = memberEntityOptional.orElseThrow(() -> new MemberNotFoundException("Member not found with email: " + email));
        return modelMapper.map(memberEntity, MemberDTO.class);
    }

      @Override
    public MemberDTO getMemberByPhone(String phone) {
        LOGGER.info("Retrieving member by phone: {}", phone);
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByPhone(phone);
        MemberEntity memberEntity = memberEntityOptional.orElseThrow(() -> new MemberNotFoundException("Member not found with phone: " + phone));
        return modelMapper.map(memberEntity, MemberDTO.class);
    }

    @Override
    public List<MemberDTO> getMembersByStatus(String status) {
        LOGGER.info("Retrieving members by status: {}", status);
        List<MemberEntity> memberEntities = memberRepository.findByMembershipStatus(status);
         return memberEntities.stream()
                .map(memberEntity -> modelMapper.map(memberEntity, MemberDTO.class))
                .collect(Collectors.toList());
    }
     @Override
    public List<MemberDTO> getMembersByName(String name) {
        LOGGER.info("Retrieving members by name: {}", name);
        List<MemberEntity> memberEntities = memberRepository.findByNameContainingIgnoreCase(name);
        return memberEntities.stream()
                .map(memberEntity -> modelMapper.map(memberEntity, MemberDTO.class))
                .collect(Collectors.toList());
    }
}
