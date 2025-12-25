package com.example.Animalgram.controller;

import com.example.Animalgram.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

//    @PostMapping("/")
//    public Api<ImageResponse> createImage(@RequestBody @Valid ImageRequest request){
//        var response = imageService.createImage(request);
//        return Api.OK(response);
//    }
//
//    @GetMapping("/popular")
}
