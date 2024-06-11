package com.ssafy.mokkoji.member.users.repository;


import com.ssafy.mokkoji.member.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Boolean existsByUserNickname(String nickname);

}