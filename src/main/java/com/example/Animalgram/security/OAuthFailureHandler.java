package com.example.Animalgram.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static com.example.Animalgram.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

@Slf4j
@Component
public class OAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        Cookie[] requestCookies = request.getCookies();
        Optional<Cookie> redirectCookie = requestCookies == null ? Optional.empty() : Arrays.stream(requestCookies)
                .filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM))
                .findFirst();

        String redirectUri = redirectCookie.map(Cookie::getValue).orElse(LOCAL_REDIRECT_URL);
        String error = URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8);
        String targetUrl = redirectUri + "/sociallogin?error=" + error;

        log.warn("OAuth login failed. targetUrl: {}", targetUrl);
        response.sendRedirect(targetUrl);
    }
}
