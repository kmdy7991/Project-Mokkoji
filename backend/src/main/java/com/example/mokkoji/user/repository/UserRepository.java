package com.example.mokkoji.user.repository;

import com.example.mokkoji.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CURD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 JpaRepository를 상속했기 때문에 IoC된다.
public interface UserRepository extends JpaRepository<User, Integer> {
    // findBy 규칙 => Username 문법
    // select * from user where username = 1?
    public User findByUsername(String username);  // jpa name 함수
}
