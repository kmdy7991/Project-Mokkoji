package com.example.mokkoji.member.api.controller.user;

import com.example.mokkoji.member.api.entity.user.Users;
import com.example.mokkoji.member.api.repository.user.UserRegistrationRequest;
import com.example.mokkoji.member.api.service.UserService;
import com.example.mokkoji.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")  // 엔드포인트
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ApiResponse getUser() {
        // 현재 인증된 사용자의 정보를 SecurityContextHolder를 통해 가져옴
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal() : 현재 인증된 principal의 정보를 가져옴
        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // userService를 통해 사용자 정보를 가져옴
        // userService.getUseer(principal.getUsername()) : 현재 사용자의 정보를 username을 기반으로 조회
        Users users = userService.getUser(principal.getUsername());

        return ApiResponse.success("user", users);
    }

    // 닉네임 중복체크
//    @GetMapping("/nickname/{nickname}")
//    public ResponseEntity<?> checkNickname(@PathVariable String nickname){
//        return ResponseEntity.ok(userService.checkNickname(nickname));
//    }

    // 닉네임 중복 체크
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable String nickname) {
        // 중복이 아니면 OK 응답, 중복이면 CONFLICT 응답
        Optional<?> result = userService.checkNickname(nickname);

        if (result.isPresent()) {
            // Optional이 비어있지 않을 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            // Optional이 비어있을 경우
            return ResponseEntity.ok().build();
        }
    }

    // 사용자 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        try {
            // 닉네임 중복 체크
            boolean isNicknameTaken = userService.checkNickname(registrationRequest.getNickname()).isPresent();
            if (isNicknameTaken) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");
            }

            // 사용자 등록
            userService.registerUser(registrationRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }

}