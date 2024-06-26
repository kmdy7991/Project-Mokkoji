package com.ssafy.mokkoji.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;


// 로그인 정보, 인증 토큰을 쿠키에 저장 -> 검색, 삭제
// 최초 인증 요청이 들어왔을 때 프론트에서 redirect 받기를 원하는 페이지로 다시 redirect 해주기 위해 cookie 사용
@Slf4j
public class CookieUtils {

    // deserialize is deprecated
    // private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        // if (cookies != null && cookies.length > 0) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<String> readServletCookie(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        log.info("cookie in {}", cookie.getAttributes());
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        // if (cookies != null && cookies.length > 0) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

//    public static String serialize(Object obj) {
//        try {
//            return Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsBytes(obj));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to serialize object to JSON", e);
//        }
//    }

//    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
//        try {
//             log.info("is cookie Value? = {}", cookie.getValue());
//             log.info("is cls what? = {}", cls.getName());
//             log.info("is cls what? = {}", objectMapper.readValue(Base64.getUrlDecoder().decode(cookie.getValue()), cls));

//            return objectMapper.readValue(Base64.getUrlDecoder().decode(cookie.getValue()), cls);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to deserialize cookie", e);
//        }
//    }

    // deserialize 수정
    // public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    //     byte[] bytes = Aes256.decrypt(cookie.getValue().getBytes(StandardCharsets.UTF_8));
    //     return (OAuth2AuthorizationRequest)SerializationUtils.deserialize(
    //     bytes);
    // }
}
