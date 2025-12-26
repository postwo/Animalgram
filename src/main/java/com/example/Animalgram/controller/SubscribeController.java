package com.example.Animalgram.controller;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;

    @PostMapping("/{toUserId}") // 누구를 구독하겠다 // 구독하는 사람 (로그인한사람) == AuthenticationPrincipal ,구독당하는 사람 PathVariable
    public Api<String> subscribe(@AuthenticationPrincipal Member member, @PathVariable int toUserId) {
        subscribeService.subscribe(member.getId(), toUserId);
        return Api.OK("구독 성공");
    }

    @DeleteMapping("/{toUserId}") // 구독 취소하기
    public Api<String> unSubscribe(@AuthenticationPrincipal Member member, @PathVariable int toUserId) {
        subscribeService.cancleSubscribe(member.getId(), toUserId);
        return Api.OK("구독 취소 성공");
    }
}
