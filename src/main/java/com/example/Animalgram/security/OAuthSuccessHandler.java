package com.example.Animalgram.security;

import com.example.Animalgram.provider.JwtTokenProvider;
import com.example.Animalgram.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.example.Animalgram.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";
    private static final int REFRESH_COOKIE_AGE = 7 * 24 * 60 * 60;

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof CustomOauth2User customOauth2User) {
            username = customOauth2User.getUsername();
        } else {
            username = authentication.getName();
        }

        var member = memberRepository.findByUsername(username).orElseThrow();
        String accessToken = jwtTokenProvider.generateAccessToken(member.getUsername(), member.getStatus());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getUsername());
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(REFRESH_COOKIE_AGE);
        response.addCookie(refreshCookie);

        Cookie[] requestCookies = request.getCookies();
        Optional<Cookie> oCookie = requestCookies == null ? Optional.empty() : Arrays.stream(requestCookies)
                .filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM))
                .findFirst();
        Optional<String> redirectUri = oCookie.map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(LOCAL_REDIRECT_URL) + "/sociallogin?token=" + accessToken;

        response.sendRedirect(targetUrl);
    }

}
