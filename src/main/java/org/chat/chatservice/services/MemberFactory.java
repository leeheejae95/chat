package org.chat.chatservice.services;

import org.chat.chatservice.entities.Member;
import org.chat.chatservice.enums.Gender;
import org.chat.chatservice.enums.MemberRole;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MemberFactory {

    public static Member create(OAuth2UserRequest userRequest, OAuth2User user) {
        return switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "kakao" -> {
                Map<String, Object> attributeMap = user.getAttribute("kakao_account");
                yield Member.builder()
                        .email(attributeMap.get("email").toString())
                        .nickName((String) ((Map) attributeMap.get("profile")).get("nickName"))
                        .name(attributeMap.get("name").toString())
                        .phoneNumber(attributeMap.get("phoneNumber").toString())
                        .gender(Gender.valueOf(attributeMap.get("gender").toString().toLowerCase()))
                        .birthDay(getDate(attributeMap))
                        .role(MemberRole.USER.getCode())
                        .build();
            }
            case "google" -> {
                Map<String, Object> attributeMap = user.getAttributes();
                yield Member.builder()
                        .email((String) attributeMap.get("email"))
                        .nickName((String) attributeMap.get("given_name"))
                        .name((String) attributeMap.get("name"))
                        .role(MemberRole.USER.getCode())
                        .build();
            }
            default -> throw new IllegalArgumentException("연동되지 않은 서비스입니다.");
        };
    }

    private static LocalDate getDate(Map<String, Object> attributeMap) {
        String birthDay = attributeMap.get("birthDay").toString();
        String birthYear = attributeMap.get("birthYear").toString();

        return LocalDate.parse(birthYear + birthDay, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
