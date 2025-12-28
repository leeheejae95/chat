package org.chat.chatservice.enums;

import java.util.Arrays;

public enum MemberRole {
    USER("USER_ROLE"),
    CONSULTANT("ROLE_CONSULTANT");

    String code;

    MemberRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MemberRole fromCode(String code) {
        return Arrays.stream(MemberRole.values())
                .filter(role -> role.getCode().equals(code))
                .findFirst()
                .orElseThrow();
    }
}
