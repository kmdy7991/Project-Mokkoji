package com.example.mokkoji.member.oauth.service;

import com.example.mokkoji.member.api.entity.user.Users;
import com.example.mokkoji.member.api.repository.user.UserRepository;
import com.example.mokkoji.member.oauth.entity.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 사용자 이름으로 DB에 사용자를 찾아서 UserPrincipal 생성
@Service
// @RequiredArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUserId(username);
        if (users == null) {
            throw new UsernameNotFoundException("Can not find username.");
        }
        return UserPrincipal.create(users);
    }
}
