package com.example.Animalgram.dto.member;

import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.domain.member.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;

    private String userName;

    private String email;

    private MemberStatus status;

    private String address;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    private LocalDateTime lastLoginAt;

    public static MemberResponse create(Member member){
        return MemberResponse.builder()
                .id(member.getId())
                .userName(member.getUsername())
                .email(member.getEmail())
                .status(member.getStatus())
                .address(member.getAddress())
                .registeredAt(member.getRegisteredAt())
                .unregisteredAt(member.getUnregisteredAt())
                .lastLoginAt(member.getLastLoginAt())
                .build();
    }


}
