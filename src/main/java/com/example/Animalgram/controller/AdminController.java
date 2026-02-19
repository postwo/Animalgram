package com.example.Animalgram.controller;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;

    @GetMapping("/dashboard")
    public String admin(){
        return "admin dashboard";
    }

    @GetMapping("/members")
    public Api<List<Member>> getAllUsers() {

        var members = memberRepository.findAll();
        return Api.OK(members);
    }

}
