package com.example.Animalgram.domain.image;

import com.example.Animalgram.domain.likes.Likes;
import com.example.Animalgram.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String caption;
    private String postImageUrl;

    @JsonIgnoreProperties({"images"}) // images는 무시해 == User 테이블에 있는 images 정보는 필요없어서 이렇게 사용
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Likes> likes;

    @CreatedDate
    @Column(name = "createDate_at", updatable = false)
    private LocalDateTime createDate;
}
