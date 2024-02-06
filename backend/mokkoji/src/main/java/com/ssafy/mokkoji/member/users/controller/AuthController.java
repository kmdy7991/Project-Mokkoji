//package com.example.mokkoji.member.users.controller;
//
//import com.example.mokkoji.common.ApiResponse;
//import com.example.mokkoji.common.utils.CookieUtils;
//import com.example.mokkoji.common.utils.HeaderUtil;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//
//// 로그인, 토큰 갱신 작업
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthTokenProvider tokenProvider;  // jwt 생성
//    private final AuthenticationManager authenticationManager;  // 사용자 인증 처리 (시큐리티)
//    private final UserRefreshTokenRepository userRefreshTokenRepository;  // 리프레쉬 토큰
//    private final static long THREE_DAYS_MSEC = 259200000;  // 3일
//    private final static String REFRESH_TOKEN = "refresh_token";
//
//    // 사용자 로그인 처리
//    // 1. 사용자의 인증 정보를 받아 authentication 객체 생성
//    // 2. accessToken, refreshToken 생성
//    // 3. refreshToken db에 저장/업뎃
//    // 4. refreshToken 쿠키에 저장 후 클라이언트에게 전달
//    @PostMapping("/login")
//    public ApiResponse login(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthReqModel authReqModel) {
//
//        // 1. 사용자의 인증 정보를 받아 authentication 객체 생성
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReqModel.getId(), authReqModel.getPassword()));
//        String userId = authReqModel.getId();
//        // 인증된 사용자를 SecurityContextHolder에 저장(현재 사용자의 인증정보는 앞으로 이걸 사용)
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//        // 2. accessToken, refreshToken 생성
//        Date now = new Date();
//        AuthToken accessToken = tokenProvider.createAuthToken(userId, ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(), new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));  // (id, role, expiry)
//        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
//        AuthToken refreshToken = tokenProvider.createAuthToken(appProperties.getAuth().getTokenSecret(), new Date(now.getTime() + refreshTokenExpiry));  // (id, expiry)
//
//
//        // 3. refreshToken db에 저장/업뎃
//        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
//        if (userRefreshToken == null) {  // 없으면 새로 등록해서 db에 즉시 저장(saveAndFlush(jpa))
//            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
//            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
//        } else {  // 있으면 db에 refresh 토큰 업데이트
//            userRefreshToken.setRefreshToken(refreshToken.getToken());
//        }
//
//        // refreshToken 쿠키에 저장 -> 클라이언트
//        int cookieMaxAge = (int) refreshTokenExpiry / 60;  // refreshToken 만료시간 (분단위)
//        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);  // (request, response, name)
//        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);  // (response, name, value, maxAge)
//        return ApiResponse.success("token", accessToken.getToken());
//    }
//
//    // 1. accessToken을 확인하고 검증
//    // 2. 만료된 accessToken인 경우 처리
//    // 3. 사용자 ID 및 역할 정보를 추출
//    @GetMapping("/refresh")
//    public ApiResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
//
//        // 1. accessToken을 확인하고 검증
//        String accessToken = HeaderUtil.getAccessToken(request);
//        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
//        if (!authToken.validate()) {
//            return ApiResponse.invalidAccessToken();
//        }
//
//
//        // 2. 만료된 accessToken인 경우 처리
//        // expired accessToken 인지 확인
//        // claim : 주체가 무엇인지 표현하는 값과 쌍
//        Claims claims = authToken.getExpiredTokenClaims();
//        if (claims == null) {
//            return ApiResponse.notExpiredTokenYet();
//        }
//
//        String userId = claims.getSubject();  // 토큰 제목
//        RoleType roleType = RoleType.of(claims.get("role", String.class));
//
//        // refreshToken
//        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN).map(Cookie::getValue).orElse((null));
//        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
//
//        if (authRefreshToken.validate()) {
//            return ApiResponse.invalidRefreshToken();
//        }
//
//        // DB 확인
//        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
//        if (userRefreshToken == null) {
//            return ApiResponse.invalidRefreshToken();
//        }
//
//        Date now = new Date();
//        AuthToken newAccessToken = tokenProvider.createAuthToken(
//                userId,
//                roleType.getCode(),
//                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
//        );
//
//        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();
//
//        // refreshToken 유효 기간이 3일 이하로 남은 경우,
//        // 새로운 accessToken을 생성하고 refreshToken 갱신
//        if (validTime <= THREE_DAYS_MSEC) {
//            // refresh 토큰 설정
//            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
//
//            authRefreshToken = tokenProvider.createAuthToken(
//                    appProperties.getAuth().getTokenSecret(),
//                    new Date(now.getTime() + refreshTokenExpiry)
//            );
//
//            // DB에 refreshToken 업데이트
//            userRefreshToken.setRefreshToken(authRefreshToken.getToken());
//
//            int cookieMaxAge = (int) refreshTokenExpiry / 60;
//            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
//            CookieUtils.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
//        }
//
//        return ApiResponse.success("token", newAccessToken.getToken());
//    }
//}
