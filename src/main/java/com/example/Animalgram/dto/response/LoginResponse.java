package com.example.Animalgram.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;


    public static LoginResponse of(String accessToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public static LoginResponse refreshOf(String accessToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

}
