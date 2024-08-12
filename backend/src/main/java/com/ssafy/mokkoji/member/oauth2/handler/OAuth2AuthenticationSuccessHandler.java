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
    private String redirectUri;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    // 에이테그 리다이랙트 백으로.
    // 성공 로그를 출력하고, jwt를 생성해 프론트에게 전달하는 역할
    // determineTargetUrl   1`메서드를 호출하여 리다이렉트할 URL을 결정
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("======= onAuthenticationSuccess 클래스 시작=======");
        log.info("로그인 인증이 성공하였습니다.");

        // 프론트엔드로 JWT를 URL 파라미터로 붙여서 리다이렉트
        String targetUrl = determineTargetUrl(request, response, authentication);
        // determineTargetUrl(request, response, authentication);
        log.info("targetUrl는 " + targetUrl + " 입니다.");

//        if (response.isCommitted()) {
//            log.debug("Response has already been committed.");
//            return;
//        }
//
//        clearAuthenticationAttributes(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        // 응답이 이미 커밋되지 않았을 때만 리다이렉트 수행
        if (!response.isCommitted()) {
            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }


        // 마지막에 프론트로 redirect 시켜주고 cors 다 열어줘야함
        // 프론트에서 꺼내서 라우터로 돌려야함 -> 코드나 클라이언트아이디, 비번 다 노출됨
        log.info("======= onAuthenticationSuccess 클래스 끝=======");
    }

    // 로그인 성공 후 리다이렉트할 URL을 결정
    // 프론트엔드에서 전달받은 리다이렉트 URL이 올바른지 확인
    // JWT를 생성하고, 해당 JWT를 URL 파라미터가 아닌 HTTP 응답 헤더에 추가
    // => 헤더말고 쿼리 스트링으로 보내기로 했다
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("======= determineTargetUrl 클래스 시작=======");
        Optional<String> redirectUris =
                CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        log.info("redirectUri : " + redirectUris);
        if (redirectUris.isPresent() && !isAuthorizedRedirectUri(redirectUris.get())) {
            throw new RuntimeException("redirect URIs are not matched.");
        }

         String targetUrl = redirectUris.orElse(redirectUri);

        // /dashboard는 로그인 후 리다이렉션해야 하는 URL, 프론트 주소
        // 해당 방법은 jwt를 쿼리 파라미터로 보내기 때문에 보안의 위험이 있고 현재 targetUrl은 '/'임
        // 현재 메서드 마지막 부분에 HTTP 응답 헤더에 JWT 추가하는 부분이 있음

        // String userName, Collection~ 추가
        String userName = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


        //JWT 생성
        // UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userName, authorities);
        log.info("jwt: " + tokenInfo.getAccessToken());  // 정상적으로 출력됨
        log.info("jwt: " + tokenInfo.getRefreshToken());

        // 리다이렉션 URL에 JWT를 쿼리 파라미터로 추가할 때 쓰는 return
        // 해당 부분을 사용하면 아래 'HTTP 응답 헤더에 JWT 추가' 부분과 'return targetUrl' 부분을 주석처리
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", tokenInfo.getAccessToken())
                .queryParam("refresh_token", tokenInfo.getRefreshToken())
                .build().toUriString();

        // HTTP 응답 헤더에 JWT 추가
//        response.addHeader("Authorization", "Bearer " + tokenInfo.getAccessToken());
//        response.addHeader("USER_ROLE", String.valueOf(tokenInfo.getRole()));

//        log.info("Line 98 = {} {}",String.valueOf(redirectUris), targetUrl);
//        return targetUrl;
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
        log.info("clientRedirectUri {}" + clientRedirectUri);
        log.info("authUri {}" + authorizedUri);
//        if (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                && authorizedUri.getPort() == clientRedirectUri.getPort()) {
//            return true;
//        }
//        return false;
//
//        return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                && authorizedUri.getPort() == clientRedirectUri.getPort();

        return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort()
                && authorizedUri.getPath().equals(clientRedirectUri.getPath())
                && authorizedUri.getQuery().equals(clientRedirectUri.getQuery());
    }
}
