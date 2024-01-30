package com.example.mokkoji.user.config.auth;

import com.example.mokkoji.user.model.User;
import com.example.mokkoji.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessiongUrl("/login");
// login 요청이 오면 자동으로 userDetailsService 타입으로 IoC 되어 있는
// loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session(Authentication(내부 UserDetails))
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        User userEntity = userRepository.findByUsername(username);  // Jpa Query methods
        if(userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
