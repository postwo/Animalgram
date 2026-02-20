package com.example.Animalgram.security.service;

import com.example.Animalgram.domain.member.Member;
import com.example.Animalgram.repository.MemberRepository;
import com.example.Animalgram.security.CustomOauth2User;
import com.example.Animalgram.security.dto.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserSservice extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String name = attributes.getName();
        String email = attributes.getEmail();
        String id = attributes.getId();

        String socialType;
        if ("naver".equals(registrationId)) {
            socialType = "naver";
        } else if ("kakao".equals(registrationId)) {
            socialType = "kakao";
        } else if ("google".equals(registrationId)) {
            socialType = "google";
        } else {
            socialType = "google";
        }

        if (name == null) {
            name = "";
        }
        if (email == null || email.isBlank()) {
            email = socialType + "_" + id + "@oauth.local";
        }
        final String normalizedEmail = email;
        final String normalizedUsername = normalizedEmail;

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Member member = memberRepository.findByEmail(normalizedEmail)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .username(normalizedUsername)
                        .email(normalizedEmail)
                        .password(null)
                        .status("ROLE_USER")
                        .build()));

        log.info("Successfully pulled user info username {} authProvider {}", normalizedUsername, socialType);

        return new CustomOauth2User(member.getId(), member.getEmail(), member.getUsername(), authorities, attributes);
    }
}
