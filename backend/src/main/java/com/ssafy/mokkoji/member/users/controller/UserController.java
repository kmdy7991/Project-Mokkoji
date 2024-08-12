package com.ssafy.mokkoji.member.users.controller;

import com.ssafy.mokkoji.member.users.repository.UserRegistrationRequest;
import com.ssafy.mokkoji.member.users.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")  // security 때문에 /user 지움
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired  // add
    private final CustomOAuth2UserService customOAuth2UserService;

    // hyunjin
    // 닉네임 중복검사
    @GetMapping("/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable("nickname") String nickname) {
        // 중복이 아니면 OK 응답, 중복이면 CONFLICT 응답
        boolean result = customOAuth2UserService.checkNickname(nickname);
        if (result) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok().body(result);
        }
    }

    // 닉네임 중복검사 후 닉네임 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        log.info("user nickname register = {}", registrationRequest);
        try {
            // 닉네임 중복 체크
            boolean isNicknameTaken = customOAuth2UserService.checkNickname(registrationRequest.getNickname());
            if (isNicknameTaken) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");
            }
            customOAuth2UserService.insertNickname(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }

    // // 테스트코드
    // @GetMapping("/testToken/{token}")
    // public ResponseEntity<String> testToken(@PathVariable("token") String token) {
    //     log.info("token in?? = {}", token);

    //     return ResponseEntity.ok().body("success");
    // }
}