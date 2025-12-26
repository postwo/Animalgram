package com.example.Animalgram.service.implement;


import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.error.TokenErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.member.*;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.service.MemberService;
import com.example.Animalgram.util.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public MemberResponse register(RegisterRequest request) {
        memberRepository.findByEmail(request.getEmail())
                .ifPresent(member -> {
                    throw new ApiException(MemberErrorCode.MEMBER_ALREADY_REGISTERED, "이미 가입된 회원 입니다");
                });
         String encodedPassword = passwordEncoder.encode(request.getPassword());
         request.setPassword(encodedPassword);
         Member member = Member.create(request);

         memberRepository.save(member);

         return MemberResponse.create(member);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
       Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND, "사용자를 찾을 수 없음."));

       if (!passwordEncoder.matches(request.getPassword(),member.getPassword())){
           throw new ApiException(MemberErrorCode.PASSWORD_NOT_MATCH, "비밀번호가 일치하지 않습니다.");
       }

       String role = member.getStatus().toString();

        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail(),role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

       return LoginResponse.login(accessToken,refreshToken);
    }

    @Override
    public LoginResponse refreshToken(HttpServletRequest request) {

        // 쿠키에서 refresh token 추출
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new ApiException(TokenErrorCode.INVALID_TOKEN,"유효하지 않은 리프레시 토큰입니다");
        }

        String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND,"사용자를 찾을 수 없음."));

        String role = member.getStatus().toString();

        if (!refreshToken.equals(member.getRefreshToken())) {
            throw new ApiException(TokenErrorCode.REFRESH_TOKEN_NOT_MATCH, "서버에 저장된 리프레시 토큰과 다릅니다.");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(email,role);

        return LoginResponse.login(newAccessToken,null);
    }

    @Override
    public String logout(String accessToken, HttpServletResponse httpResponse) {
        String token = accessToken.replace("Bearer ", "");
        String email = jwtTokenProvider.getUsernameFromToken(token);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND, "사용자를 찾을수 없습니다"));

        member.setRefreshToken(null);
        memberRepository.save(member);

        // 쿠키 삭제
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        httpResponse.addCookie(refreshCookie);

        return "로그아웃 성공";
    }

}
