package com.ssafy.mokkoji.common.utils;

import jakarta.servlet.http.HttpServletRequest;

// Header에서 accessToken 추출
public class HeaderUtil {
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    // Authorization 헤더값을 읽어 accessToken 추출
    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);
        if (headerValue == null) {  // 존재하지 않거나 "Bearer"로 시작하지 않으면 null 리턴
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());  // "Bearer" 제거하고 나머지 문자열 리턴
        }

        return null;
    }
}
