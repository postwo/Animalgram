package com.example.Animalgram.service;

import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.dto.member.MemberProfileResponse;
import com.example.Animalgram.dto.member.MyUpdateResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageService {

    MemberProfileResponse profile(int memberId, Member member);

    void updateProfileImageUrl(int memberId, MultipartFile profileImageUrl);


    MyUpdateResponse profileUpdate(int id, Member member);
}
