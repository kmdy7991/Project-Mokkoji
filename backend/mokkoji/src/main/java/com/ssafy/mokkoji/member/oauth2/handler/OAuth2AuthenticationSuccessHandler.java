package com.ssafy.mokkoji.member.oauth2.handler;


import com.ssafy.mokkoji.common.utils.CookieUtils;
import com.ssafy.mokkoji.member.jwt.JwtTokenProvider;
import com.ssafy.mokkoji.member.users.dto.UserResponseDto;
import com.ssafy.mokkoji.member.users.repository.CookieAuthorizationRequestRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import static com.ssafy.mokkoji.member.users.repository.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth.authorizedRedirectUri}")
    // @Value("/oauth2/authorize")
    private String redirectUri;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;


    // 성공 로그를 출력하고, jwt를 생성해 프론트에게 전달하는 역할
    // determineTargetUrl 메서드를 호출하여 리다이렉트할 URL을 결정
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("======= onAuthenticationSuccess 클래스 시작=======");
        log.info("로그인 인증이 성공하였습니다.");

        // 프론트엔드로 JWT를 URL 파라미터로 붙여서 리다이렉트
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("targetUrl는 " + targetUrl + " 입니다.");
        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        log.info("======= onAuthenticationSuccess 클래스 끝=======");
    }

    // 로그인 성공 후 리다이렉트할 URL을 결정
    // 프론트엔드에서 전달받은 리다이렉트 URL이 올바른지 확인
    // JWT를 생성하고, 해당 JWT를 URL 파라미터가 아닌 HTTP 응답 헤더에 추가

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("======= determineTargetUrl 클래스 시작=======");
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("redirect URIs are not matched.");
        }

        // String targetUrl = redirectUri.orElse("/dashboard");
        // /dashboard는 로그인 후 리다이렉션해야 하는 URL, 프론트 주소
        // 해당 방법은 jwt를 쿼리 파라미터로 보내기 때문에 보안의 위험이 있고
        // 현재 targetUrl은 '/'임
        // 현재 메서드 마지막 부분에 HTTP 응답 헤더에 JWT 추가하는 부분이 있음
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());


        // String userName, Collection~ 추가
        String userName = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();



        //JWT 생성
        // UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userName, authorities);
        log.info("jwt: " + tokenInfo.getAccessToken());  // 성공함

        // 리다이렉션 URL에 JWT를 쿼리 파라미터로 추가할 때 쓰는 return
        // 해당 부분을 사용하면 아래 HTTP 응답 헤더에 JWT 추가 부분과 return 부분을 주석처리
//        return UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("token", tokenInfo.getAccessToken())
//                .build().toUriString();

        // HTTP 응답 헤더에 JWT 추가
        response.addHeader("Authorization", "Bearer " + tokenInfo.getAccessToken());
        response.addHeader("USER_ROLE", String.valueOf(tokenInfo.getRole()));
        return targetUrl;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        log.info("사용자의 인증정보와 쿠키를 제거합니다.");
    }

    // 프론트엔드에서 전달받은 리다이렉트 URI가 올바른지 확인
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);
        if (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }
}
