package com.example.Animalgram.controller;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.dto.request.LoginRequest;
import com.example.Animalgram.dto.request.SignupRequest;
import com.example.Animalgram.dto.response.LoginResponse;
import com.example.Animalgram.dto.response.SignupResponse;
import com.example.Animalgram.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    // 컨트롤러에서 구현체에 직접의존하는 방식은 안좋다 그러므로 인터페이스 사용
    private final MemberService memberService;

    @PostMapping("/signup")
    public Api<SignupResponse> signup(@RequestBody @Valid SignupRequest request){
        var response = memberService.signup(request);
        return Api.OK(response);
    }

    @PostMapping("/login")
    public Api<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse hsResponse){
        var response = memberService.login(request,hsResponse);
        return Api.OK(response);
    }

    @PostMapping("/refresh")
    public Api<LoginResponse> refresh(HttpServletRequest request) {
        var response = memberService.refresh(request);
        return Api.OK(response);
    }

    @PostMapping("/logout")
    public Api<String> logout(@RequestHeader("Authorization") String accessToken,HttpServletResponse response){
        memberService.logout(accessToken,response);
        return Api.OK("로그아웃 성공");
    }
}
