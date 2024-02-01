package com.example.mokkoji;

import com.example.mokkoji.member.config.properties.AppProperties;
import com.example.mokkoji.member.config.properties.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CorsProperties.class,
        AppProperties.class
})

// @SpringBootApplication
public class MokkojiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MokkojiApplication.class, args);
    }

}
