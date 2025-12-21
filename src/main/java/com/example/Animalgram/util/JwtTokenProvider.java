package com.example.Animalgram.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret; // JWT 서명용 비밀키

    @Value("${jwt.access-expiration-time}")
    private long accessTokenExpiration; // 액세스 토큰 만료 시간(ms)

    @Value("${jwt.refresh-expiration-time}")
    private long refreshTokenExpiration; // 리프레시 토큰 만료 시간(ms)

    private SecretKey getSigningKey() {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 문자열 키를 바이트 배열로 변환
        return key; // JWT 서명용 SecretKey 생성
    }

    public String generateAccessToken(String email,String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role) // 권한 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)) // 30분
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)) // 7일
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(token).getBody();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
