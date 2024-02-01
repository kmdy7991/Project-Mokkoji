package com.example.mokkoji.member.oauth.entity;

import lombok.Getter;

// 소셜 로그인 타입
@Getter
public enum ProviderType {
    GOOGLE,
    NAVER,
    KAKAO;
}