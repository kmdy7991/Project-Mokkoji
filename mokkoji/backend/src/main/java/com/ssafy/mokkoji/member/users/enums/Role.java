package com.ssafy.mokkoji.member.users.enums;

import lombok.Getter;

@Getter
public enum Role {

    GUEST("게스트"),
    USER("사용자"),
    ADMIN("관리자");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
