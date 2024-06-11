package com.ssafy.mokkoji.member.oauth2.handler;


import com.ssafy.mokkoji.common.utils.CookieUtils;
import com.ssafy.mokkoji.member.users.repository.CookieAuthorizationRequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.ssafy.mokkoji.member.users.repository.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        log.info("======= onAuthenticationFailure 클래스 시작=======");
        log.info("로그인 인증이 실패했습니다.");
        // targetUrl : 로그인 실패 후 리다이렉트될 URL, 현재는 "/"로 정의
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("/");

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", authenticationException.getLocalizedMessage())
                .build().toUriString();

        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        log.info("로그인을 실패하였습니다. 사용된 쿠키를 제거합니다.");
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        log.info("최종적으로 구성된 URL로 리다이렉트를 수행합니다.");
        log.info("======= onAuthenticationFailure 클래스 끝=======");
    }
}

