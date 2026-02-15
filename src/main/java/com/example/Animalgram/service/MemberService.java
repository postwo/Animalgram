package com.example.Animalgram.service;

import com.example.Animalgram.dto.request.LoginRequest;
import com.example.Animalgram.dto.request.SignupRequest;
import com.example.Animalgram.dto.response.LoginResponse;
import com.example.Animalgram.dto.response.SignupResponse;

public interface MemberService {
    SignupResponse signup(SignupRequest request);
    LoginResponse login(LoginRequest request);
}
