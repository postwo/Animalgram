package com.example.Animalgram.dto.image;

import com.example.Animalgram.domain.member.Member;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Data
public class ImageUploadRequest {
    private MultipartFile file;
    private String caption;

    public Image toEntity (String postImageUrl, Member member) {
        return Image.builder()
                .caption(caption)
                .postImageUrl(postImageUrl)
                .member(member) // 어떤 유저가 insert(저장)를 했는지 알기위해 유저정보가 필요하다
                .build();
    }
}
