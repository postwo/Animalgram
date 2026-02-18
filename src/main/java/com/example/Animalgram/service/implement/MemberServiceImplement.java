package com.example.Animalgram.service.implement;

import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.error.TokenErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.request.LoginRequest;
import com.example.Animalgram.dto.request.SignupRequest;
import com.example.Animalgram.dto.response.LoginResponse;
import com.example.Animalgram.dto.response.SignupResponse;
import com.example.Animalgram.provider.JwtTokenProvider;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImplement implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder  passwordEncoder;

    public Member getByUsernameOrThrow(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        MemberErrorCode.MEMBER_NOT_FOUND, "사용자가 없습니다."
                ));
    }


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
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse hsResponse) {
        var member = memberRepository.findByEmail(request.getEmail()).orElseThrow(()->new ApiException(MemberErrorCode.MEMBER_NOT_FOUND,"사용자가 없습니다"));

        if(!passwordEncoder.matches(request.getPassword(),member.getPassword())){
            throw new ApiException(MemberErrorCode.PASSWORD_NOT_MATCH,"비밀번호가 일치하지 않습니다");
        }

        var accessToken = jwtTokenProvider.generateAccessToken(member.getUsername());
        var refreshToken = jwtTokenProvider.generateRefreshToken(member.getUsername());

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        Cookie refreshCookie = new Cookie("refreshToken",member.getRefreshToken());
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(7*24*60*60); // 7일
        hsResponse.addCookie(refreshCookie);// 응답할때 쿠키도 담아서 보낸다


        return LoginResponse.of(accessToken);
    }

    @Override
    @Transactional
    public LoginResponse refresh(HttpServletRequest request) {
        String refreshToken =null;
        if (request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if (cookie.getName().equals("refreshToken")){
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)){
            throw new ApiException(TokenErrorCode.INVALID_REFRESH_TOKEN,"refresh Token이 유효하지 않습니다");
        }

        var username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        var member = getByUsernameOrThrow(username);

       if (!refreshToken.equals(member.getRefreshToken())){
           throw new ApiException(TokenErrorCode.REFRESH_TOKEN_NOT_MATCH,"서버에 저장된 refresh Token과 일치하지 않습니다");
       }

        var newAccessToken = jwtTokenProvider.generateAccessToken(username);
        var newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        member.setRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return LoginResponse.refreshOf(newAccessToken);
    }

    @Override
    @Transactional
    public void logout(String accessToken, HttpServletResponse response) {
        var token = accessToken.replace("Bearer ", "");
        var username = jwtTokenProvider.getUsernameFromToken(token);
        var member=  getByUsernameOrThrow(username);

        member.setRefreshToken(null);
        memberRepository.save(member);

        Cookie refreshCookie = new Cookie("refreshToken",null);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
    }
}
