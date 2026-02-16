package com.example.Animalgram.controller;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.dto.request.LoginRequest;
import com.example.Animalgram.dto.request.SignupRequest;
import com.example.Animalgram.dto.response.LoginResponse;
import com.example.Animalgram.dto.response.SignupResponse;
import com.example.Animalgram.service.MemberService;
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
    public Api<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        var response = memberService.login(request);
        return Api.OK(response);
    }

    //aceestoken이 만료되면 refreshtoken을 받아서 accesstoken을 재발급
    // Authorization이 키값을 사용해서 header에있는 refreshtoken값을 가지고 온다
    @PostMapping("/refresh")
    public Api<LoginResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        var response = memberService.refresh(refreshToken);
        return Api.OK(response);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String accessToken){
        memberService.logout(accessToken);
        return "로그아웃 성공";
    }
}
