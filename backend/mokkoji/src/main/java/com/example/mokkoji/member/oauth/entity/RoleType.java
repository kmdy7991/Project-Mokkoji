package com.example.mokkoji.member.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

// 사용자 역할을 관리, 식별
@Getter
@AllArgsConstructor
public enum RoleType {
    USER("USER", "사용자 권한"),
    ADMIN("ADMIN", "관리자 권한"),
    GUEST("GUEST", "게스트 권한");

    private final String code;  // 각 역할에 대한 고유 코드
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}
