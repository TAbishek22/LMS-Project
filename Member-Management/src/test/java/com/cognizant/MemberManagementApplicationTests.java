
package com.cognizant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cognizant.dto.MemberDTO;
import com.cognizant.dto.MemberDisplayDTO;
import com.cognizant.entity.MemberEntity;
import com.cognizant.exception.*;
import com.cognizant.repository.MemberRepository;
import com.cognizant.service.serviceimpl.MemberServiceImpl;

class MemberManagementApplicationTests {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMember_Success() {
        MemberDisplayDTO dto = new MemberDisplayDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("plainPwd");

        MemberEntity entity = new MemberEntity();
        MemberEntity savedEntity = new MemberEntity();
        savedEntity.setMemberId(1L);

        MemberDisplayDTO savedDto = new MemberDisplayDTO();
        savedDto.setMemberId(1L);

        when(memberRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPwd");
        when(modelMapper.map(dto, MemberEntity.class)).thenReturn(entity);
        when(memberRepository.save(entity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, MemberDisplayDTO.class)).thenReturn(savedDto);

        MemberDisplayDTO result = memberService.addMember(dto);

        assertEquals(1L, result.getMemberId());
        verify(memberRepository).save(entity);
    }

    @Test
    void testLogin_Success() {
        String email = "user@example.com";
        String password = "plainPwd";
        String encodedPassword = "encodedPwd";

        MemberEntity entity = new MemberEntity();
        entity.setEmail(email);
        entity.setPassword(encodedPassword);

        MemberDTO dto = new MemberDTO();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(modelMapper.map(entity, MemberDTO.class)).thenReturn(dto);

        MemberDTO result = memberService.login(email, password);

        assertNotNull(result);
        verify(modelMapper).map(entity, MemberDTO.class);
    }

    @Test
    void testGetAllMembers() {
        List<MemberEntity> entities = List.of(new MemberEntity());
        when(memberRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(any(MemberEntity.class), eq(MemberDTO.class))).thenReturn(new MemberDTO());

        List<MemberDTO> result = memberService.getAllMembers();

        assertEquals(1, result.size());
    }

    @Test
    void testGetMemberEmail_Success() {
        MemberEntity entity = new MemberEntity();
        entity.setEmail("abc@example.com");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(entity));

        String result = memberService.getMemberEmail(1L);

        assertEquals("abc@example.com", result);
    }

    @Test
    void testUpdateMember_Success() {
        MemberEntity entity = new MemberEntity();
        entity.setMemberId(1L);

        MemberDTO dto = new MemberDTO();
        MemberEntity updatedEntity = new MemberEntity();
        updatedEntity.setMemberId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            MemberDTO source = invocation.getArgument(0);
            MemberEntity target = invocation.getArgument(1);
            return null;
        }).when(modelMapper).map(any(MemberDTO.class), eq(entity));
        when(memberRepository.save(entity)).thenReturn(updatedEntity);
        when(modelMapper.map(updatedEntity, MemberDTO.class)).thenReturn(dto);

        MemberDTO result = memberService.updateMember(1L, dto);

        assertNotNull(result);
    }

    @Test
    void testDeleteMember_Success() {
        when(memberRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> memberService.deleteMember(1L));

        verify(memberRepository).deleteById(1L);
    }

    @Test
    void testGetMemberById_Success() {
        MemberEntity entity = new MemberEntity();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, MemberDTO.class)).thenReturn(new MemberDTO());

        MemberDTO result = memberService.getMemberById(1L);

        assertNotNull(result);
    }

    @Test
    void testGetMemberByEmail_Success() {
        MemberEntity entity = new MemberEntity();
        when(memberRepository.findByEmail("x@x.com")).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, MemberDTO.class)).thenReturn(new MemberDTO());

        MemberDTO result = memberService.getMemberByEmail("x@x.com");

        assertNotNull(result);
    }

    @Test
    void testGetMemberByPhone_Success() {
        MemberEntity entity = new MemberEntity();
        when(memberRepository.findByPhone("1234567890")).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, MemberDTO.class)).thenReturn(new MemberDTO());

        MemberDTO result = memberService.getMemberByPhone("1234567890");

        assertNotNull(result);
    }

    @Test
    void testGetMembersByStatus() {
        List<MemberEntity> entities = List.of(new MemberEntity());
        when(memberRepository.findByMembershipStatus("active")).thenReturn(entities);
        when(modelMapper.map(any(MemberEntity.class), eq(MemberDTO.class))).thenReturn(new MemberDTO());

        List<MemberDTO> result = memberService.getMembersByStatus("active");

        assertEquals(1, result.size());
    }

    @Test
    void testGetMembersByName() {
        List<MemberEntity> entities = List.of(new MemberEntity());
        when(memberRepository.findByNameContainingIgnoreCase("john")).thenReturn(entities);
        when(modelMapper.map(any(MemberEntity.class), eq(MemberDTO.class))).thenReturn(new MemberDTO());

        List<MemberDTO> result = memberService.getMembersByName("john");

        assertEquals(1, result.size());
    }
}


