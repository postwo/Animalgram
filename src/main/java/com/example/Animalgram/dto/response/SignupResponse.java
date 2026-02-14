package com.example.Animalgram.dto.response;

import com.example.Animalgram.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    private Long id;
    private String email;
    private String username;

    public static SignupResponse of(Member member){
        return SignupResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }

}
