package com.ssafy.mokkoji.common.config;


import com.ssafy.mokkoji.member.jwt.JwtAuthenticationFilter;
import com.ssafy.mokkoji.member.jwt.JwtTokenProvider;
import com.ssafy.mokkoji.member.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.ssafy.mokkoji.member.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.ssafy.mokkoji.member.users.repository.CookieAuthorizationRequestRepository;
import com.ssafy.mokkoji.member.users.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// Spring Security 환경 설정을 구성하기 위한 클래스
// 웹 서비스가 로드될 때 Spring Container 의해 관리가 되는 클래스
// 사용자에 대한 인증, 인가에 대한 구성을 Bean 메서드로 주입
@Configuration
@RequiredArgsConstructor  // 생성자의 파라미터 순서가 클래스에 선언된 순서와 일치해야 함
@EnableWebSecurity  // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class WebSecurityConfigure {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        System.out.println("필터체인?");
        //httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http
                // csrf 방지 (csrf 보호 기능 비활성화)
                // jwt 방식을 제대로 쓰려고 하면 프론트엔드가 분리된 환경을 가정하고 해야함
                .csrf(AbstractHttpConfigurer::disable
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // cors 설정
                // 세션 관리 정책
                // 모든 요청 허용할거다
                // jwt 방식은 세션저장을 하지 않기 때문에 꺼준다
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 로그인 안쓸거다
                .formLogin(AbstractHttpConfigurer::disable
                )
                // 시큐리티 로그인 페이지 안씀
                .httpBasic(AbstractHttpConfigurer::disable
                )

                //요청에 대한 권한 설정
                .authorizeHttpRequests((authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        // /user/로 시작되는 주소는 USER 권한을 가진 사용자만 접근 가능
                                        .requestMatchers("/user/*").hasAnyAuthority("ROLE_USER")
                                        .anyRequest().permitAll()
//                                 .requestMatchers("/oauth2/*").permitAll()
//                                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )


                .oauth2Login((oauth2Login) ->
                                        oauth2Login
                                        // oauth2Login.authorizationEndpoint(auth -> auth.baseUri("/*/oauth2/code/*"))
                                        // Oauth2가 제공하는 로그인 페이지를 사용하지 않고 프론트 주소로 보냄
                                        // 프론트는 '로그인 버튼'을 클릭할 때 href를 '/oauth2/authorization/{provider}' 여기로 보내야 하고
                                        // developer 사이트에서 서비스 주소를 프론트, 콜백주소를 백으로 해야함(인가코드를 받아야 하니까)
//                                        .loginPage("http://192.168.31.57:5173")  // 성주님  http://192.168.105.15:5173/
//                                        .loginPage("http://192.168.31.22:5173/")
//                                        .loginPage("http://localhost:5173")

                                        .authorizationEndpoint(authorizationEndpoint ->
                                                authorizationEndpoint
//                                                         .baseUri("/oauth2/authorization/{provider}")  // 소셜 로그인창
//                                                        .baseUri("/oauth2/authorize")
                                                        .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                                        )

//                                        .redirectionEndpoint(redirectionEndpoint ->
//                                                        redirectionEndpoint
//                                                                .baseUri("/login/oauth2/code/{provider}")
                                                // .baseUri("/oauth2/code/{provider}")
//                                                .baseUri("/oauth2/callback/*")
//                                        )

                                        .userInfoEndpoint(userInfoEndpoint ->
                                                userInfoEndpoint
                                                        .userService(customOAuth2UserService)  // 회원 저장
                                        )
                                        .successHandler(oAuth2AuthenticationSuccessHandler)  // jwt 생성
                                        .failureHandler(oAuth2AuthenticationFailureHandler)
                );

        System.out.println("로긴성공");

        http
                .logout(logout ->
                        logout
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                );

        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        System.out.println("끝");
        return http.build();
    }

    // cors(cross-origin-resource-sharing
    // origin - 스키마 + 도메인(+포트)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 접근 url 설정, 추후 허용하는 서버로 접근 지정
        config.setAllowedOriginPatterns(List.of("*"));

        // 허용할 rest api 목록, HttpMethod로 추후 허용하는 method만 추가
        config.setAllowedMethods(List.of("*"));

        // 허용할 헤더 목록(client에 보낼), 추후 사용하는 header만 지정 "X-Requested-With", "Content-Type", "Authorization", "X-XSRF-token"
        config.setAllowedHeaders(List.of("*"));

        // client에 노출 시킬 헤더(이 설정 없으면 client에서 해당 헤더를 가져오지 못함) (cors에서 몇몇 헤더외엔 노출 되지 않음) (cors에서 헤더 - 향후 헤더에 담아 보낼 key값 정의)
        config.setExposedHeaders(List.of("*"));

        // 요청에 쿠키나 인증 토큰(jwt) 포함가능
        config.setAllowCredentials(true);

        /*
         * cross-origin 요청 전 options 메소드로 preflight 전송하는데
         * 이 결과를 지정한 시간동안 저장하도록 지정
         * */
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 지정한 패턴의 요청에 위에서 지정한 cors 정책 적용
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}