package jwtSecurity.example.jwtDemo.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jwtSecurity.example.jwtDemo.Config.JwtTokenProvider;
import jwtSecurity.example.jwtDemo.Config.MemberClient;
import jwtSecurity.example.jwtDemo.Dto.LoginDto;
import jwtSecurity.example.jwtDemo.Dto.MemberDTO;
import jwtSecurity.example.jwtDemo.Service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginDto loginDto) {
        // 01 - Call Member Service via Feign Client to validate credentials
        MemberDTO memberDTO = memberClient.loginMember(loginDto.getEmail(), loginDto.getPassword());

        // 02 - Check if multiple members exist with the same email
        
        // 03 - If valid, create authentication token manually (or just use email)
        String token = jwtTokenProvider.generateTokenFromEmail(memberDTO.getEmail());

        return token;
    }

}