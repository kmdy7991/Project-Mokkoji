package com.example.mokkoji.member.api.repository.user;

import com.example.mokkoji.member.api.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 사용자 정보를 DB에 조회
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUserId(String userId);
    Optional<Boolean> existsByUserNickname(String nickname);
}