package com.example.Animalgram.domain.comment;

import com.example.Animalgram.domain.image.Image;
import com.example.Animalgram.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String content; //내용

    @JoinColumn(name = "imageId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Image image;

    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @CreatedDate
    @Column(name = "createDate_at", updatable = false)
    private LocalDateTime createDate;

}
