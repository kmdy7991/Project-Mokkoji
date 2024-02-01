package com.example.mokkoji.member.api.service;

import com.example.mokkoji.member.api.entity.user.Users;
import com.example.mokkoji.member.api.repository.user.UserRegistrationRequest;
import com.example.mokkoji.member.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 사용자 정보를 DB에 조회
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Users getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<?> checkNickname(String nickname){
        return userRepository.existsByUserNickname(nickname);
    }

    // 추가함
    public void registerUser(UserRegistrationRequest registrationRequest) {
        // User 엔터티 생성 및 필요한 정보 설정
        Users newUser = new Users();
        newUser.setUserId(registrationRequest.getUserId());
        newUser.setUserNickname(registrationRequest.getNickname());
        // 다른 필요한 정보들 설정

        // UserRepository를 통해 저장
        userRepository.save(newUser);
    }
}
