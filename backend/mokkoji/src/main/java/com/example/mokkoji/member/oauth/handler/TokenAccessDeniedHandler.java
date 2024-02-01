package com.example.mokkoji.member.oauth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

// 권한이 없는 요청에 대한 처리를 담당
// 사용자가 특정 리소스에 대한 권한이 없을 때 처리되는 방식
@Component
//@RequiredArgsConstructor
//@Primary
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolve;

    public TokenAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolve) {
        this.resolve = resolve;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        resolve.resolveException(request, response, null, accessDeniedException);
    }
}