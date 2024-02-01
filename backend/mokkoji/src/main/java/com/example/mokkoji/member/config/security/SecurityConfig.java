package com.example.mokkoji.member.config.security;

import com.example.mokkoji.member.api.repository.user.UserRefreshTokenRepository;
import com.example.mokkoji.member.config.properties.AppProperties;
import com.example.mokkoji.member.config.properties.CorsProperties;
import com.example.mokkoji.member.oauth.entity.RoleType;
import com.example.mokkoji.member.oauth.exception.RestAuthenticationEntryPoint;
import com.example.mokkoji.member.oauth.filter.TokenAuthenticationFilter;
import com.example.mokkoji.member.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.example.mokkoji.member.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.example.mokkoji.member.oauth.handler.TokenAccessDeniedHandler;
import com.example.mokkoji.member.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.mokkoji.member.oauth.service.CustomOAuth2UserService;
import com.example.mokkoji.member.oauth.service.CustomUserDetailsService;
import com.example.mokkoji.member.oauth.token.AuthTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// Spring Security 환경 설정을 구성하기 위한 클래스
// 웹 서비스가 로드될 때 Spring Container 의해 관리가 되는 클래스
// 사용자에 대한 ‘인증’과 ‘인가’에 대한 구성을 Bean 메서드로 주입
@Configuration
// @RequiredArgsConstructor  // 생성자의 파라미터 순서가 클래스에 선언된 순서와 일치해야 함
@AllArgsConstructor
@EnableWebSecurity  // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig {

    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    // UserDetailsService 설정
//    @Override
//    @Bean
//    protected AuthenticationManagerBuilder configure(@Qualifier("authenticationManagerBuilder") AuthenticationManagerBuilder auth) throws Exception {  // configure data type : void -> AuthenticationManagerBuilder
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//        return auth;
//    }

    // SecurityFilterChain : HTTP 보안 구성 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf 방지 (csrf 보호 기능 비활성화)
                // jwt 방식을 제대로 쓰려고 하면 프론트엔드가 분리된 환경을 가정하고 해야함
                .csrf(AbstractHttpConfigurer::disable
                )

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

                // 인증 진입점, 접근 거부 핸들러
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(new RestAuthenticationEntryPoint())    // 인증 진입점 설정
                                .accessDeniedHandler(tokenAccessDeniedHandler)  // 접근 거부 핸들러 설정
                )

                // 요청 권한 설정
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                                authorizeHttpRequests
//                                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()  // Preflight 요청 허용
//                                        .requestMatchers("/api/*").hasAnyAuthority(RoleType.USER.getCode())
//                                        .requestMatchers("/api/*/admin/*").hasAnyAuthority(RoleType.ADMIN.getCode())
//                                        // .requestMatchers("/*").permitAll()
//                                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
//                                        // .anyRequest().permitAll()  // 비회원 ?
//                )

                // 인가, 리다이렉션, 사용자정보 엔드포인트
                .oauth2Login((oauth2Login) ->
                        oauth2Login
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.baseUri("/oauth2/authorization")
                                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                                )

                                .redirectionEndpoint(redirectionEndpoint ->
                                        redirectionEndpoint.baseUri("/*/oauth2/code/*")
                                )
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(oAuth2UserService)
                                )
                                .defaultSuccessUrl("/nickname")  // 추가
                                .successHandler(oAuth2AuthenticationSuccessHandler())
                                .failureHandler(oAuth2AuthenticationFailureHandler())
                )

                .logout((logoutConfig) ->
                        logoutConfig.logoutSuccessUrl("/")
                );

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                    .cors()
//                .and()
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                .and()
//                    .csrf().disable()
//                    .formLogin().disable()
//                    .httpBasic().disable()
//                    .exceptionHandling()
//                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())  // 인증 진입점 설정
//                    .accessDeniedHandler(tokenAccessDeniedHandler)  // 접근 거부 핸들러 설정
//
//                .and()
//                    .authorizeRequests()
//                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()  // Preflight 요청 허용
//                    .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode())
//                    .antMatchers("/api/**/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode())
//                    // .anyRequest().authenticated()  // 나머지 요청은 인증 필요
//                    .anyRequest().permitAll()
//                .and()
//                    .oauth2Login()
//                    .authorizationEndpoint()
//                    .baseUri("/oauth2/authorization")
//                    .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
//                .and()
//                    .redirectionEndpoint()
//                    .baseUri("/*/oauth2/code/*")
//                .and()
//                    .userInfoEndpoint()
//                    .userService(oAuth2UserService)
//                .and()
//                    .successHandler(oAuth2AuthenticationSuccessHandler())
//                    .failureHandler(oAuth2AuthenticationFailureHandler());
//
//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }

    // auth 매니저 설정
    // @Override
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }

    // 사용자 정보를 기반으로 인증 매니저 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // security 설정시 사용할 인코더 설정
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 토큰 필터 설정
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    // 쿠키 기반 인가 Repository
    // 인가 응답을 연계하고 검증할때 사용
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    // OAuth 인증 성공 핸들러
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
                userRefreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    // OAuth 인증 실패 핸들러
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    // Cors 설정
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/*", corsConfig);
        return corsConfigSource;
    }
}
