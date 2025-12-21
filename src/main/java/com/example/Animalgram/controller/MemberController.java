package com.example.Animalgram.controller;


import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.dto.member.LoginRequest;
import com.example.Animalgram.dto.member.LoginResponse;
import com.example.Animalgram.dto.member.MemberResponse;
import com.example.Animalgram.dto.member.RegisterRequest;
import com.example.Animalgram.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public Api<MemberResponse> register(@RequestBody @Valid RegisterRequest request){
        var response = memberService.register(request);
        return Api.OK(response);
    }

    @PostMapping("/login")
    public Api<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse httpResponse){
        var response = memberService.login(request);

        // Refresh Token을 HttpOnly 쿠키로 설정
        Cookie refreshCookie = new Cookie("refreshToken", response.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일

        httpResponse.addCookie(refreshCookie);

        return Api.OK(response);
    }

    @PostMapping("/refresh")
    public Api<LoginResponse> refresh(HttpServletRequest request) {
        var response = memberService.refreshToken(request);
        return Api.OK(response);
    }

    @PostMapping("/logout")
    public Api<String> logout(@RequestHeader("Authorization") String accessToken,
                                         HttpServletResponse httpResponse) {
        var response = memberService.logout(accessToken,httpResponse);
        return Api.OK(response);
    }
}
