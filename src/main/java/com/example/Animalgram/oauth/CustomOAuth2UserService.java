package com.example.Animalgram.oauth;

import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.oauth.dto.GoogleResponse;
import com.example.Animalgram.oauth.dto.MemberDTO;
import com.example.Animalgram.oauth.dto.NaverResponse;
import com.example.Animalgram.oauth.dto.OAuth2Response;
import com.example.Animalgram.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Member existData= memberRepository.findByUsername(username).orElse(null);

        // 신규 사용자면 회원가입
        if (existData == null) {
            Member member = Member.createOauth2Member(oAuth2Response.getName(), oAuth2Response.getEmail(), username);
            memberRepository.save(member);

            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setRole("ROLE_"+member.getStatus());
            memberDTO.setName(member.getName());
            memberDTO.setUsername(member.getUsername());

            return new CustomOAuth2User(memberDTO);
        }

        else { // 이미 가입된 사용자라면 이메일과 이름 정보를 최신 정보로 업데이트
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            memberRepository.save(existData);

            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setRole("ROLE_"+existData.getStatus());
            memberDTO.setName(existData.getName());
            memberDTO.setUsername(existData.getUsername());

            return new CustomOAuth2User(memberDTO);
        }
    }
}
