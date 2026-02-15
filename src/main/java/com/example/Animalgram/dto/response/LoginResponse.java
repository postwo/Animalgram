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
    private String email;
    private String username;

    public static LoginResponse of(String accessToken, Member member) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }

}
