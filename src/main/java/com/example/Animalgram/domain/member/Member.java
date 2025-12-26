package com.example.Animalgram.domain.member;


import com.example.Animalgram.domain.BaseTimeEntity;
import com.example.Animalgram.domain.image.Image;
import com.example.Animalgram.domain.member.enums.MemberStatus;
import com.example.Animalgram.dto.member.RegisterRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "member_name", nullable = false, length = 50)
    private String memberName;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    private String profileImageUrl;
    private String website; //웹사이트
    private String bio; //자기소개

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus status;

    private boolean social;

    private String refreshToken;

    @Column(name = "unregistered_at")
    private LocalDateTime unregisteredAt; // 탈퇴 시간

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})
    private List<Image> images;


    public static Member create(RegisterRequest request){
        return Member.builder()
                .email(request.getEmail())
                .memberName(request.getMemberName())
                .password(request.getPassword())
                .status(MemberStatus.USER)
                .social(false)
                .build();
    }

    public static Member createOauth2Member(String name,String memberName,String eamil){
        return Member.builder()
                .memberName(memberName)
                .email(eamil)
                .status(MemberStatus.USER)
                .social(true)
                .build();
    }

}
