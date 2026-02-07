package com.example.Animalgram.domain.image;

import com.example.Animalgram.domain.BaseTimeEntity;
import com.example.Animalgram.domain.comment.Comment;
import com.example.Animalgram.domain.likes.Likes;
import com.example.Animalgram.domain.member.Member;
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
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private String postImageUrl;

    @JsonIgnoreProperties({"images"}) // images는 무시해 == User 테이블에 있는 images 정보는 필요없어서 이렇게 사용
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Likes> likes;

    @OrderBy("id DESC")//이렇게 정렬할수 있다
    @JsonIgnoreProperties({"image"})//무한참조 발생 해서 추가 ,Image 엔티티에서 List<Likes> likes;를 가져올 때, 각 Likes 객체 내부에 있는 image 필드는 무시
    @OneToMany(mappedBy = "image") //연관관계의 주인은 comment 안의 있는 image 변수 이다
    private List<Comment> comments;

    @Transient //DB에 컬럼이 만들어지지 않는다 이걸 만든이유는 프론트 단에서 like 표시를 변경해주기 위해 사용
    private boolean likeState;

    @Transient //DB에 컬럼이 만들어지지 않는다 이걸 만든이유는 프론트 단에서 like 표시를 변경해주기 위해 사용
    private int likeCount;
}
