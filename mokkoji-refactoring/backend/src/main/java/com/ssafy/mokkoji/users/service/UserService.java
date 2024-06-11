package com.ssafy.mokkoji.users.service;


import com.ssafy.mokkoji.global.jwt.JwtTokenProvider;
import com.ssafy.mokkoji.users.entity.Users;
import com.ssafy.mokkoji.users.mapper.UserMapper;
import com.ssafy.mokkoji.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper; // mybatis

    public int isSameNickname(String nickname) {
        return userMapper.isSameNickname(nickname);
    }

    public int guestInput(String nickname) {
        return userMapper.guestInput(nickname);
    }

    public Users isAdmin(String email) {
        return userMapper.isAdmin(email);
    }
}