package com.ssafy.mokkoji.member.users.controller;

import com.ssafy.mokkoji.member.users.entity.Users;
import com.ssafy.mokkoji.member.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> getUserByEmail(@RequestBody Map<String, String> map) {
        Users user = userService.isAdmin(map.get("email"));
        log.info("user in = {}, email in = {}", user, map.get("email"));

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are not an ADMIN.");
        }
        return ResponseEntity.ok().body("ssafy1234");
    }
}