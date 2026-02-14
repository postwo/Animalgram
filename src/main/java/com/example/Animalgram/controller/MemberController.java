package com.example.Animalgram.controller;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.dto.request.SignupRequest;
import com.example.Animalgram.dto.response.SignupResponse;
import com.example.Animalgram.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
