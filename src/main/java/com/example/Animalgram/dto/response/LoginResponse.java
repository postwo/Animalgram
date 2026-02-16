package com.example.Animalgram.dto.response;

import com.example.Animalgram.domain.member.Member;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String username;

    public static LoginResponse of(String accessToken, String refreshToken,Member member) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }

    public static LoginResponse refreshOf(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
