package com.example.Animalgram.service.implement;

import com.example.Animalgram.common.error.ImageErrorCode;
import com.example.Animalgram.common.error.MemberErrorCode;
import com.example.Animalgram.common.exception.ApiException;
import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.member.MemberProfileResponse;
import com.example.Animalgram.dto.member.MyUpdateResponse;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.repository.SubscribeRepository;
import com.example.Animalgram.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    @Value("${file.path}")
    private String uploadFolder;
    private final MemberRepository memberRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder PasswordEncoder;


    @Override
    @Transactional(readOnly = true) //읽기전용 = 데이터변경감지를 안한다
    public MemberProfileResponse profile(int memberId, Member member) {

        MemberProfileResponse memberProfileResponse = new MemberProfileResponse();

        String email = member.getEmail();
        int principalid = member.getId();

        Member myMember = memberRepository.findByEmail(email)
                .orElseThrow(() ->{
                    throw new ApiException(MemberErrorCode.MEMBER_NOT_FOUND, "사용자를 찾을 수 없음.");
                        });

        memberProfileResponse.setMember(myMember);
        memberProfileResponse.setPageOwnerState(principalid == memberId);
        memberProfileResponse.setImageCount(myMember.getImages().size());


        int SubscribeState = subscribeRepository.mSubscribeState(principalid,memberId);
        int SubscribeCount = subscribeRepository.mSubscribeCount(memberId);

        memberProfileResponse.setSubscribeState(SubscribeState == 1);
        memberProfileResponse.setSubscribeCount(SubscribeCount);

        // 마이페이지 이미지에 좋아요 카운트 추가하기
        // 각각의 이미지마다 들고 있어야 하기 때문에 dto에 따로 넣을 수도없고 해서 dto안에 User에 memberntity 내부를 수정해주면 된다
        member.getImages().forEach(image -> {
            image.setLikeCount(image.getLikes().size());
        });

        return memberProfileResponse;
    }

    @Override
    public void updateProfileImageUrl(int memberId, MultipartFile profileImageUrl) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid+"_"+profileImageUrl.getOriginalFilename();
        log.info("imageFileName : {}",imageFileName);

        Path imageFilePath = Paths.get(uploadFolder+imageFileName);

        try{
            Files.write(imageFilePath, profileImageUrl.getBytes());
        } catch (IOException e) {
            throw new ApiException(ImageErrorCode.FILE_UPLOAD_FAILED,e);
        }


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    throw new ApiException(MemberErrorCode.MEMBER_NOT_FOUND, "사용자를 찾을 수 없음.");
                });

        member.setProfileImageUrl(imageFileName);
    }

    @Override
    @Transactional
    public MyUpdateResponse profileUpdate(int id, Member member) {
        Member memberEntity = memberRepository.findById(id).orElseThrow(()-> {
            throw new ApiException(MemberErrorCode.MEMBER_NOT_FOUND, "사용자를 찾을 수 없음.");
        });

        memberEntity.setMemberName(member.getMemberName());
        memberEntity.setPassword(PasswordEncoder.encode(member.getPassword()));
        memberEntity.setWebsite(member.getWebsite());
        memberEntity.setBio(member.getBio());

        return MyUpdateResponse.myProfileUpdate(memberEntity);
    }


}
