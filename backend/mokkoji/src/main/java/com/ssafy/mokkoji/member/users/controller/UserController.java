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
@RequestMapping("/api")  // security 때문에 /user 지움
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired  // add
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;

//    @PostMapping("/login")
//    public UserResponseDto.TokenInfo login(@RequestBody UserLoginRequestDto memberLoginRequestDto) {
//        String memberId = memberLoginRequestDto.getUserId();
//        String password = memberLoginRequestDto.getPassword();
//        UserResponseDto.TokenInfo tokenInfo = userService.login(memberId, password);
//        return tokenInfo;
//    }


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

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
//        log.info("user nickname register = {}", registrationRequest);
//
//        try {
//            // 사용자 정보 업데이트
//            customOAuth2UserService.updateUserNickname(registrationRequest);
//            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
//        }
//    }



//    // yejin
//    // 익명 유저 등록
//    @GetMapping("/register/{user_nickname}")
//    public ResponseEntity<?> registerGuest(@PathVariable("user_nickname") String user_nickname){
//        try{
//            if(userService.isSameNickname(user_nickname) == 1){
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");
//            }
//            // 사용자 등록
//            if(userService.guestInput(user_nickname) == 1){
//                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
//            }
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("User registered failed.");
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
//        }
//    }

}