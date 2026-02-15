package com.example.Animalgram.service.implement;

import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.request.LoginRequest;
import com.example.Animalgram.dto.request.SignupRequest;
import com.example.Animalgram.dto.response.LoginResponse;
import com.example.Animalgram.dto.response.SignupResponse;
import com.example.Animalgram.provider.JwtTokenProvider;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImplement implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder  passwordEncoder;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        String email = request.getEmail();
        boolean existedEmail = memberRepository.existsByEmail(email);
        if (existedEmail) {
            throw new ApiException(MemberErrorCode.DUPLICATE_EMAIL);
        }

        String username = request.getUsername();
        boolean existedUsername = memberRepository.existsByUsername(username);
        if (existedUsername) {
            throw new ApiException(MemberErrorCode.DUPLICATE_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        var member = Member.createMember(request,encodedPassword);


        var signupMember = memberRepository.save(member);

        return SignupResponse.of(signupMember);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        var member = memberRepository.findByEmail(request.getEmail()).orElseThrow(()->new ApiException(MemberErrorCode.MEMBER_NOT_FOUND,"사용자가 없습니다"));

        if(!passwordEncoder.matches(request.getPassword(),member.getPassword())){
            throw new ApiException(MemberErrorCode.PASSWORD_NOT_MATCH,"비밀번호가 일치하지 않습니다");
        }

        var accessToken = jwtTokenProvider.generateToken(member.getUsername());

        return LoginResponse.of(accessToken,member);
    }
}
