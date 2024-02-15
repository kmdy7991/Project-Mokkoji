package com.ssafy.mokkoji.member.users.controller;

import com.ssafy.mokkoji.member.users.dto.UserLoginRequestDto;
import com.ssafy.mokkoji.member.users.dto.UserResponseDto;
import com.ssafy.mokkoji.member.users.repository.UserRegistrationRequest;
import com.ssafy.mokkoji.member.users.service.CustomOAuth2UserService;
import com.ssafy.mokkoji.member.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;


    @PostMapping("/login")
    public UserResponseDto.TokenInfo login(@RequestBody UserLoginRequestDto memberLoginRequestDto) {
        String memberId = memberLoginRequestDto.getUserId();
        String password = memberLoginRequestDto.getPassword();
        return userService.login(memberId, password);
    }


    // 닉네임 중복
    @GetMapping("/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable("nickname") String nickname) {
        // 중복이 아니면 OK 응답, 중복이면 CONFLICT 응답
        boolean result = customOAuth2UserService.checkNickname(nickname);  // optional<?> -> boolean
        if (result) {  // ispresent() 없앰
            // Optional이 비어있지 않을 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            // Optional이 비어있을 경우
            return ResponseEntity.ok().body(result);
        }
    }

    // 사용자 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        log.info("user register = {}", registrationRequest);
        try {
            // 닉네임 중복 체크
            boolean isNicknameTaken = customOAuth2UserService.checkNickname(registrationRequest.getNickname());  // isPresent() delete
            if (isNicknameTaken) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");
            }

            // 사용자 등록
            customOAuth2UserService.insertNickname(registrationRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }


// 익명 유저 등록
    @GetMapping("/register/{userNickname}")
    public ResponseEntity<?> registerGuest(@PathVariable("userNickname") String userNickname){
        try{
            if(userService.isSameNickname(userNickname) == 1){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");
            }
            // 사용자 등록
            if(userService.guestInput(userNickname) == 1){
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User registered failed.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }
}