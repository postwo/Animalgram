package com.example.Animalgram.service;

import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.image.ImageUploadRequest;
import com.example.Animalgram.dto.image.PopularImageResponse;

import java.util.List;

public interface ImageService {
    List<PopularImageResponse> popularImage();

    void upload(ImageUploadRequest imageUploadRequest, Member member);
}
