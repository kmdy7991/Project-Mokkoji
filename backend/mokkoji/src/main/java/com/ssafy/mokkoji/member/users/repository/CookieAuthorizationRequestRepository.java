package com.ssafy.mokkoji.member.users.repository;


import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.ssafy.mokkoji.common.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

// cookie를 사용하는 것은 최초 인증 요청이 들어왔을 때
// 프론트에서 redirect 받기를 원하는 페이지로 다시 redirect 해주기 위해
// 해당 요청 값을 저장하기 위한 목적
@Component
@Slf4j
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    // 인증 요청 정보를 쿠키에서 로드, 쿠키가 없으면 null 반환
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("====== CookieAuthorizationRequestRepository 진입 ======");
        log.info("====== loadAuthorizationRequest 시작 ======");
        log.info("인증 요청");
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }


    // OAuth 2.0 인증 요청 정보를 쿠키에 저장, authorizationRequest가 null이면 쿠키 삭제, 리다이렉트 URI도 쿠키에 저장
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("====== saveAuthorizationRequest 시작 ======");
        log.info("인증 저장");
        if (authorizationRequest == null) {
            System.out.println("authorizationRequest가 null이다??");
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
        // String redirectUriAfterLogin = authorizationRequest.getRedirectUri();
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        // System.out.println("redirectUriAfterLogin: " + redirectUriAfterLogin);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }
        log.info("====== saveAuthorizationRequest 끝 ======");
    }

    // 인증 요청 정보를 삭제하고 해당 정보를 반환
    // 실제로는 loadAuthorizationRequest를 호출해서 정보를 가져오고 쿠키를 삭제
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("====== removeAuthorizationRequest 시작 ======");
        log.info("인증 삭제. 바로 removeAuthorizationRequest 끝");
        // return null;
        return this.loadAuthorizationRequest(request);
    }
//
//    @Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
//        return this.loadAuthorizationRequest(request);
//    }

    // 쿠키에서 OAuth 2.0 인증 요청, 리다이렉트 URI 모두 삭제
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        log.info("====== removeAuthorizationRequestCookies 시작 ======");
        System.out.println("쿠키삭제");
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        log.info("====== removeAuthorizationRequestCookies 끝 ======");
    }
}

