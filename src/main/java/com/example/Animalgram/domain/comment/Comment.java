package com.example.Animalgram.domain.comment;

import com.example.Animalgram.domain.BaseTimeEntity;
import com.example.Animalgram.domain.image.Image;
import com.example.Animalgram.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String content; //내용

    @JoinColumn(name = "imageId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Image image; //어떤이미지에 작성했는지

    @JsonIgnoreProperties({"images"}) // 다른 정보는가지고 와도 images이정보는 필요없기 떄문에 빼고 가지고오기
    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member; //누가 작성했는지

}
