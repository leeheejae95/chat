package org.chat.chatservice.services;

import lombok.RequiredArgsConstructor;
import org.chat.chatservice.entities.Member;
import org.chat.chatservice.enums.Gender;
import org.chat.chatservice.repositories.MemberRepository;
import org.chat.chatservice.vo.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> map = oAuth2User.getAttribute("kakao_account");
        String email = map.get("email").toString();
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> registerMember(map));

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }

    private Member registerMember(Map<String, Object> attributeMap) {
        Member member = Member.builder()
                .email(attributeMap.get("email").toString())
                .nickName((String) ((Map) attributeMap.get("profile")).get("nickName"))
                .name(attributeMap.get("name").toString())
                .phoneNumber(attributeMap.get("phoneNumber").toString())
                .gender(Gender.valueOf(attributeMap.get("gender").toString().toLowerCase()))
                .birthDay(getDate(attributeMap))
                .role(attributeMap.get("role").toString())
                .build();
        return member;
    }

    private LocalDate getDate(Map<String, Object> attributeMap) {
        String birthDay = attributeMap.get("birthDay").toString();
        String birthYear = attributeMap.get("birthYear").toString();

        return LocalDate.parse(birthYear + birthDay, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
