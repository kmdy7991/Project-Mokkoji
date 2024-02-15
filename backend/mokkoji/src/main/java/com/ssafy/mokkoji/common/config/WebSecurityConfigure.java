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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security 환경 설정을 구성하기 위한 클래스
// 웹 서비스가 로드될 때 Spring Container 의해 관리가 되는 클래스
// 사용자에 대한 ‘인증’과 ‘인가’에 대한 구성을 Bean 메서드로 주입
@RequiredArgsConstructor  // 생성자의 파라미터 순서가 클래스에 선언된 순서와 일치해야 함
@EnableWebSecurity  // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@Configuration
public class WebSecurityConfigure {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http
                // csrf 방지 (csrf 보호 기능 비활성화)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션 관리 정책
                // jwt 방식은 세션저장을 하지 않기 때문에 꺼준다
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 로그인 폼 X
                .formLogin(AbstractHttpConfigurer::disable)

                // 시큐리티 로그인 페이지 X
                .httpBasic(AbstractHttpConfigurer::disable)

                //요청에 대한 권한 설정
                // requestMatchers에 uri 매핑이후 role에 맞게 권한 부여(해당 권한 유저만 접근가능)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests

//                        .requestMatchers("/user/*").hasAnyAuthority("ROLE_USER")
                        .anyRequest().permitAll())

                // oauth2 설정

                .oauth2Login(oauth2Login ->
                                oauth2Login.authorizationEndpoint(authorizationEndpoint
                                                        -> authorizationEndpoint.authorizationRequestRepository(cookieAuthorizationRequestRepository)
//                                                 .baseUri("/oauth2/authorization/{provider}")
                                                // .baseUri("/oauth2/authorize")
                                                // .baseUri("https://192.168.31.57:5173/")
                                                // .baseUri("${oauth.authorizedRedirectUri}")
                                )

//                                .redirectionEndpoint(redirectionEndpoint ->
//                                                redirectionEndpoint.baseUri("/login/oauth2/code/{provider}")
                                        // .baseUri("/oauth2/code/{provider}")
                                        // .baseUri("/oauth2/callback/*")
//                                )

                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))  // 회원 저장
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                );
//        System.out.println("로긴성공");
//
        http.logout(
                logout -> logout
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
        );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        System.out.println("끝");

        return http.build();
    }
}