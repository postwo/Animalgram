package com.example.Animalgram.controller;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.member.MemberProfileResponse;
import com.example.Animalgram.dto.member.MyUpdateRequest;
import com.example.Animalgram.dto.member.MyUpdateResponse;
import com.example.Animalgram.dto.subscribe.SubscribeResponse;
import com.example.Animalgram.service.MyPageService;
import com.example.Animalgram.service.SubscribeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {
    private final MyPageService myPageService;
    private final SubscribeService subscribeService;

    @GetMapping("/{memberId}")
    public Api<MemberProfileResponse> profile(@PathVariable int memberId, @AuthenticationPrincipal Member member) {
        var response = myPageService.profile(memberId,member);
        return Api.OK(response);
    }

    // 프로필 이미지 업데이트
    @PutMapping("/{memberId}/profileImageUrl")
    public Api<String> profileImageUrlUpdate(@PathVariable int memberId, MultipartFile profileImageUrl) {
         myPageService.updateProfileImageUrl(memberId,profileImageUrl);
        return Api.OK("프로필이미지 수정 완료");
    }

    // 구독 리스트
    @GetMapping("{pageMemberId}/subscribe")
    public Api<List<SubscribeResponse>> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal Member member){

        var response = subscribeService.subscribeList(member,pageUserId) ;

        return Api.OK(response);
    }

    // 프로필 수정
    @PutMapping("/{id}")
    public Api<MyUpdateResponse> myProfileUpdate(@PathVariable int id, @Valid @RequestBody MyUpdateRequest myUpdateRequest){
       var response = myPageService.profileUpdate(id,myUpdateRequest.myUpdate());
       return Api.OK(response);
    }

}
