package com.example.Animalgram.service;


import com.example.Animalgram.dto.member.LoginRequest;
import com.example.Animalgram.dto.member.LoginResponse;
import com.example.Animalgram.dto.member.MemberResponse;
import com.example.Animalgram.dto.member.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    MemberResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(HttpServletRequest request);

    String logout(String accessToken, HttpServletResponse httpResponse);

//    MemberProfileResponse profile(int pageMemberId, Member member);
}
