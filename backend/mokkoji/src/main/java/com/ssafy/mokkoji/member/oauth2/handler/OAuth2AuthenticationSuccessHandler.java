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


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("여기냐?");
        log.info("targetUrl");
//        log.info(request + " " + response + " " + authentication);
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.debug("targetUrl" + targetUrl);
        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // login
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("determine");
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("redirect URIs are not matched.");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());


        /*
        String userName, Collection~ 추가
         */

        String userName = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();



        //JWT 생성
        // UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userName, authorities);
        log.info("jwt: " + tokenInfo.getAccessToken());  // 성공함

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", tokenInfo.getAccessToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        log.info("쿠키, 클리어 ??");
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

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
