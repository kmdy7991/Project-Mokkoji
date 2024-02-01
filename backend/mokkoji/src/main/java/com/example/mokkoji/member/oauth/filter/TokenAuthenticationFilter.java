package com.example.mokkoji.member.oauth.filter;

import com.example.mokkoji.member.oauth.token.AuthToken;
import com.example.mokkoji.member.oauth.token.AuthTokenProvider;
import com.example.mokkoji.member.utils.HeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//  HTTP 요청에서 추출한 액세스 토큰을 사용해 사용자를 인증하고,
//  인증된 사용자 정보를 Spring Security의 SecurityContextHolder에 저장
//  사용자의 인증, 권한 부여 담당
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;

    // 모든 HTTP 요청에 대해 한 번씩 호출
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  throws ServletException, IOException {
        // HeaderUtil.getAccessToken(request)
        // : 클라이언트로부터 전달받은 HTTP 요청에서 액세스 토큰을 헤더에서 추출
        String tokenStr = HeaderUtil.getAccessToken(request);
        // tokenProvider.convertAuthToken(tokenStr)
        // : 추출된 토큰 문자열을 사용해 AuthTokenProvider를 통해 AuthToken 객체를 생성
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);

        if (token.validate()) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }

}