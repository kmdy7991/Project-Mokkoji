package com.example.mokkoji.member.oauth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

// jwt 토큰 생성, 유효성 검사, 클레임 검색
// 역할 정보는 토큰 claim에 포함
@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;  // jwt 토큰
    private final Key key;  // jwt 암호화 키

    private static final String AUTHORITIES_KEY = "role";  // jwt claim에 역할을 저장할 때 사용되는 키

    // 역할 X
    AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    // 역할 O
    AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }

    // 주제, 클레임, 지정된 키로 서명 및 만료를 설정해 jwt 토큰을 생성
    // 역할 x
    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)  // jwt 토큰 바꾸는 방식
                .setExpiration(expiry)
                .compact();
    }

    // 역할 O
    private String createAuthToken(String id, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    // 유효성 검사, 검색
    public boolean validate() {
        // 토큰 클레임이 null인지 아닌지 확인
        return this.getTokenClaims() != null;
    }

    // jwt 예외 로깅
    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    // ExpiredJwtException을 처리하고 만료된 토큰에서 claim 반환
    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }
}
