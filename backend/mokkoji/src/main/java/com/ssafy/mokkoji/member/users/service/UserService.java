package com.ssafy.mokkoji.member.users.service;


import com.ssafy.mokkoji.member.jwt.JwtTokenProvider;
import com.ssafy.mokkoji.member.users.dto.UserResponseDto;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper; // mybatis

//    userId 없어 ㅠㅠㅠㅠ
//    public Users getUser(String userId) {
//        return userRepository.findByUserId(userId);
//    }
//
//    public boolean checkNickname(String nickname){  // optional<?> -> boolean으로 변경
//        return userRepository.existsByUserNickname(nickname);
//    }

    // 추가함 - setUserId, getUserId 에러 나서 주석처리함 ㅠㅠ
//    public void registerUser(UserRegistrationRequest registrationRequest) {
//        // User 엔터티 생성 및 필요한 정보 설정
//        Users newUser = new Users();
//        newUser.setUserId(registrationRequest.getUserId());
//        newUser.setUserNickname(registrationRequest.getNickname());
//        // 다른 필요한 정보들 설정
//
//        // UserRepository를 통해 저장
//        userRepository.save(newUser);
//    }

    public int isSameNickname(String nickname){
        return userMapper.isSameNickname(nickname);
    }

    public int guestInput(String nickname){
        return userMapper.guestInput(nickname);
    }






    // hyunjin
    @Transactional
    public UserResponseDto.TokenInfo login(String memberId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        /*
        String userName, Collection~ 추가
         */

        String userName = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userName, authorities);

        return tokenInfo;
    }
}