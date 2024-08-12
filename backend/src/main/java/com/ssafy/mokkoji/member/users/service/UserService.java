package com.ssafy.mokkoji.member.users.service;


import com.ssafy.mokkoji.member.jwt.JwtTokenProvider;
import com.ssafy.mokkoji.member.users.dto.UserResponseDto;
import com.ssafy.mokkoji.member.users.entity.Users;
import com.ssafy.mokkoji.member.users.mapper.UserMapper;
import com.ssafy.mokkoji.member.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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