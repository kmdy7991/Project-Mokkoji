package com.ssafy.mokkoji.member.users.controller;

import com.ssafy.mokkoji.member.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/guest")
@RequiredArgsConstructor
@Slf4j
public class GuestController {
    private final UserService userService;
    // 익명 유저 등록
    @GetMapping("/register/{user_nickname}")
    public ResponseEntity<?> registerGuest(@PathVariable("user_nickname") String user_nickname){
        System.out.println(user_nickname);
        try{
            if(userService.isSameNickname(user_nickname) == 1){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");
            }
            // 사용자 등록
            if(userService.guestInput(user_nickname) == 1){
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registered failed.");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }
}
