package com.example.mokkoji.user.config;

import com.example.mokkoji.user.config.auth.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// 로그인 완료된 후
// 1. 토큰받기(인증)
// 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보 가져오고
// 4. 그 정보로 회원가입 자동 진행
// 5. 추가 정보 받기


@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableMethodSecurity(securedEnabled = true)  // secured 어노테이션 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf 방지
                .csrf(AbstractHttpConfigurer::disable
                )

                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/user/**").authenticated()  // 인증된 사용자만
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")  // amdin, manager만
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")  // admin만
                                .anyRequest().permitAll()
                )

                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/loginForm")
                                .loginProcessingUrl("/login")  // login 주소가 호출되면 시큐리티가 대신
                                .defaultSuccessUrl("/joinForm")
                )

                .oauth2Login((oauth2Login) ->
                        oauth2Login
                                .loginPage("/loginForm")
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                // tip. 코드 x (엑세스토큰 + 사용자프로필정보 O)
                                                .userService(principalOauth2UserService)
                                )
                )

                .logout((logoutConfig) ->
                        logoutConfig.logoutSuccessUrl("/")
                );

        return http.build();
    }
}
