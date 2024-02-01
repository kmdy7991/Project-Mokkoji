package com.example.mokkoji.member.oauth.token;

import com.example.mokkoji.member.oauth.exception.TokenValidFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// JWT 토큰을 생성하고 변환, 해당 토큰을 사용해 스프링 시큐리티 인증 객체를 생성하는 데 사용
// 사용자를 인증하고 권한 부여
@Slf4j
public class AuthTokenProvider {

    private final Key key;  // jwt 암호화 키
    private static final String AUTHORITIES_KEY = "role";  // jwt claim에 역할을 저장할 때 사용되는 키

    public AuthTokenProvider(String secret) {
        // 주어진 비밀키를 사용해 key 초기화
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    // 스프링 시큐리티 Authentication 객체를 생성
    public Authentication getAuthentication(AuthToken authToken) {

        if(authToken.validate()) {
            // 토큰의 유효성을 확인하고, claim에서 권한 정보를 추출해 인증 객체를 생성
            Claims claims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            log.debug("claims subject := [{}]", claims.getSubject());
            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        }
        else {
            // 유효성 검사에 실패하면 TokenValidFailedException을 던짐
            throw new TokenValidFailedException();
        }
    }
}
