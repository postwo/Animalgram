package com.example.Animalgram.filter;


import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    // 여기서 권한을 넣는거는 SecurityContext에 인증 정보 + 권한 저장 하고서
    // 이후 모든 요청 처리 과정에서 SecurityContext의 권한 사용 가능하다
    // 사용하는 방법은
    // ex)
    /*

    // Spring Security가 내부적으로 하는 일
    @PreAuthorize("hasRole('ADMIN')")  // 이 어노테이션이
    public void adminMethod() {
    // SecurityContext에서 권한 꺼내서 체크
    // ADMIN 없으면 AccessDeniedException 던짐
    }

    또 다른 방법으로는

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // 권한 체크
    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

     이런 방식이 있다


    */

    // 요청 쿠키에 JWT가 존재하는 경우 JWT를 검증하고 강제로SecurityContextHolder에 세션을 생성한다. (이 세션은 STATLESS 상태로 관리되기 때문에 해당 요청이 끝나면 소멸 된다.)
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getUsernameFromToken(token);
            Member member = memberRepository.findByEmail(email)
                    .orElse(null); // 토큰은 유효하지만 사용자가 없을 수도 있음

            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_"+member.getStatus())
            );

            if (member != null) {
                // 인증 정보 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                member, null, authorities);
                /* UsernamePasswordAuthenticationToken 에 권한을 안넣은상태로 null을 하면

                    Security Config 체크 부분 여기서
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    SecurityContext에 ADMIN 권한이 없음 → 403 Forbidden 에러가 뜬다
                */

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
