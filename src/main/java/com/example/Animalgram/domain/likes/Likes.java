package com.example.Animalgram.domain.likes;

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
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        name = "likes_uk",
                        columnNames={"imageId","memberId"}
                )
        }
)
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "imageId")
    @ManyToOne
    private Image image;

    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "memberId")
    @ManyToOne
    private Member user; //누가 좋아요를 했는지

    @CreatedDate
    @Column(name = "createDate_at", updatable = false)
    private LocalDateTime createDate;
}

