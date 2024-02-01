package com.example.mokkoji.member.oauth.exception;


// 토큰 서명 오류나 토큰 유효성 검증 실패시
public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super("Failed to generate Token.");
    }

    private TokenValidFailedException(String message) {
        super(message);
    }
}
