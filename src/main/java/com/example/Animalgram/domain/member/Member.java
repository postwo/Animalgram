package com.example.Animalgram.domain.member;


import com.example.Animalgram.domain.BaseTimeEntity;
import com.example.Animalgram.domain.image.Image;
import com.example.Animalgram.domain.member.enums.MemberStatus;
import com.example.Animalgram.dto.request.SignupRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    private String profileImageUrl;
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus status;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"member"})
    private List<Image> images;

    public static Member createMember(SignupRequest request,String encodedPassword) {
        return Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .status(MemberStatus.ROLE_USER)
                .build();
    }



}
