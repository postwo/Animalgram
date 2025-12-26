package com.example.Animalgram.service.implement;

import com.example.Animalgram.domain.image.Image;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.image.ImageUploadRequest;
import com.example.Animalgram.dto.image.PopularImageResponse;
import com.example.Animalgram.repository.ImageRepository;
import com.example.Animalgram.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${file.path}")
    private String uploadFolder;
    private final ImageRepository imageRepository;

    @Override
    public List<PopularImageResponse> popularImage() {
        List<Image> images= imageRepository.mPopular();
        return  images.stream().map(
                image -> PopularImageResponse.fromEntity(image)
        ).collect(Collectors.toList());
    }

    @Override
    public void upload(ImageUploadRequest imageUploadRequest, Member member) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid+"_"+imageUploadRequest.getFile().getOriginalFilename();
        Path imageFilePath = Paths.get(uploadFolder+imageFileName);

        // 통신,i/o( 하드디스크에 기록을 하거나 읽을때) -> 예외가 발생할수 있다 그러므로 try catch로 묶는다.
        try{
            Files.write(imageFilePath, imageUploadRequest.getFile().getBytes());//사진을 바이트화 해야한다.
        }catch (Exception e){
            e.printStackTrace();
        }

        Image image = imageUploadRequest.toEntity(imageFileName,member); // db에는 이미지 파일네임을 넣을거다
        imageRepository.save(image);
    }
}
