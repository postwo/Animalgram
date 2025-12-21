package com.example.Animalgram.oauth;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.util.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
//OAuth2 로그인 성공 후 JWT 토큰을 생성하고 쿠키에 담아 리다이렉트하는 핸들러
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    //OAuth2 로그인이 성공하면 자동으로 호출되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {

        //인증 객체에서 사용자 정보를 추출합니다. getPrincipal()은 앞서 CustomOAuth2UserService에서 반환했던 CustomOAuth2User 객체이다
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        // 사용자의 권한 목록
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 권한 컬렉션을 순회하기 위한 Iterator를 생성
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        // 첫 번째 권한을 가져와서 role에 담는다
        GrantedAuthority auth = iterator.next();

        // 권한 문자열(예: "ROLE_USER")을 추출
        String role = auth.getAuthority();

        String accessToken = jwtTokenProvider.generateAccessToken(username, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND,"사용자가 없습니다"));
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        // 생성한 accesstoken
        response.sendRedirect("http://localhost:5173/?accessToken=" + accessToken);

        // refreshToken이라는 키를 생성
        response.addCookie(createRefreshCookie("refreshToken", refreshToken));

        //프론트엔드 주소(React 개발 서버)로 리다이렉트
        response.sendRedirect("http://localhost:5173/");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*30); //쿠키 유효기간을 1시간으로 설정
        //cookie.setSecure(true); HTTPS에서만 쿠키 전송 (현재 주석 처리 - 개발 환경이라 HTTP 사용 중)
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private Cookie createRefreshCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}