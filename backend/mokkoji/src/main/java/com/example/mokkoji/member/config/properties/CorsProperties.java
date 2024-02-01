package com.example.mokkoji.member.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

// 클라이언트에서 서버로의 요청에 대한 교차 출처 요청을 허용하는 정책을 제어
@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String allowedOrigins;  // 허용된 도메인 목록
    private String allowedMethods;  // 허용된 HTTP 메서드 목록
    private String allowedHeaders;  // 허용된 HTTP 헤더 목록
    private Long maxAge;  // Preflight 요청에서 결과가 캐시될 시간
}
