package com.example.Animalgram.controller;

import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.common.error.ImageErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.image.ImageUploadRequest;
import com.example.Animalgram.dto.image.PopularImageResponse;
import com.example.Animalgram.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    // 인기 이미지 페이지
    @GetMapping("/popular")
    public Api<List<PopularImageResponse>> popular(){
        var responses = imageService.popularImage();
        return Api.OK(responses);
    }

    @PostMapping("/")
    public Api<String> imageUpload(@RequestBody ImageUploadRequest imageUploadRequest , @AuthenticationPrincipal Member member){
        if (imageUploadRequest.getFile().isEmpty()){
            throw new ApiException(ImageErrorCode.IMAGE_NOT_ATTACHED,"이미지가 첨부 되지 않았습니다");
        }

        imageService.upload(imageUploadRequest,member);

        return Api.OK("이미지 업로드 성공");
    }
}
