package com.example.mokkoji.member.oauth.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

// 사용자가 인증되지 않은 상태(로그인x, 인증되지 않은 토큰 사용)에서 리소스에 접근할때 커스텀 진입점(Entry Point)
// 클라이언트에게 로그인 페이지로 이동하거나 인증 정보를 제공하도록 유도할 때
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    // 인증 예외가 발생했을 때 클라이언트한테 예외 보냄
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        authException.printStackTrace();
        log.info("Responding with unauthorized error. Message := {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
    }
}
