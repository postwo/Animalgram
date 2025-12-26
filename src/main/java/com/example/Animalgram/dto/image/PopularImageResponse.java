package com.example.Animalgram.dto.image;

import com.example.Animalgram.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularImageResponse {
    private int id;
    private String caption;
    private String postImageUrl;
    private int memberId;

    private int likeCount;
    private boolean likeState;

    public static PopularImageResponse fromEntity(Image image) {
        return PopularImageResponse.builder()
                .id(image.getId())
                .caption(image.getCaption())
                .postImageUrl(image.getPostImageUrl())
                .memberId(image.getMember().getId())
                .likeCount(image.getLikeCount())
                .likeState(image.isLikeState())
                .build();
    }
}
