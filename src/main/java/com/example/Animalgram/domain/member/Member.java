package com.example.Animalgram.domain.member;


import com.example.Animalgram.domain.BaseTimeEntity;
import com.example.Animalgram.domain.image.Image;
import com.example.Animalgram.domain.member.enums.MemberStatus;
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

    @Column(nullable = false, length = 100)
//    @Size(max = 50, message = "닉네임은 100자 이내로 입력해주세요.")
    private String nickname;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    private String profileImageUrl;
    private String bio; //자기소개

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus status;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"member"})
    private List<Image> images;


}
