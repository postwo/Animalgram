package com.example.Animalgram.security;

import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /* - member.getStatus() == null
            status 값 자체가 없는 경우
          - member.getStatus().isBlank()
            status가 "", "   "처럼 비어있거나 공백만 있는 경우
        */
        if (member.getStatus() == null || member.getStatus().isBlank()) {
            throw new ApiException(MemberErrorCode.MEMBER_NOT_ROLE, "권한이 없습니다");
        }

        return List.of(new SimpleGrantedAuthority(member.getStatus()));

//        다중 권한(예):
//
//        return member.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .toList();


    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member getMember() {
        return member;
    }
}
