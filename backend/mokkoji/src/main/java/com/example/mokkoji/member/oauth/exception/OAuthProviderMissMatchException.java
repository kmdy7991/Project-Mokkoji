package com.example.mokkoji.member.oauth.exception;


// ProviderType이 일치하지 않을 때 발생하는 예외
public class OAuthProviderMissMatchException extends RuntimeException {

    public OAuthProviderMissMatchException(String message) {
        super(message);
    }
}
