package com.example.Animalgram.security;

import com.example.Animalgram.provider.JwtTokenProvider;
import com.example.Animalgram.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)){
            var username = jwtTokenProvider.getUsernameFromToken(token);
            var member = memberRepository.findByUsername(username).orElse(null);

            UserDetails memberDetails =  userDetailsService.loadUserByUsername(username);

            if (member != null){
                // 나중에 권한 처리하기
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberDetails,null,memberDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }

    // header에 있는 토큰을 빼오는거다
    private String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }
}
