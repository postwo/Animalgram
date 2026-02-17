package com.example.Animalgram.security.service;

import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 로그인 인증 시 사용자 조회
    // 조회한 member를 userdetails(=customDetails)로 변환
    /* 즉, MemberRepository를 시큐리티가 직접 모르니까, 그 사이를 연결해주는 어댑터 역할입니다.
      폼로그인, AuthenticationManager 기반 로그인, 테스트 인증 흐름에서 특히 필요*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var member = memberRepository.findByUsername(username).orElseThrow(()->new ApiException(MemberErrorCode.MEMBER_NOT_FOUND,"사용자가 없습니다"));
        return new CustomUserDetails(member); // 시큐리티가 이해하는 형태로 변환
    }
}
