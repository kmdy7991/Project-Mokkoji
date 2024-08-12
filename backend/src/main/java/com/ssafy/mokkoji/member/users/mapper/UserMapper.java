package com.ssafy.mokkoji.member.users.mapper;

import com.ssafy.mokkoji.member.users.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int isSameNickname(String nickname);

    int guestInput(String nickname);

    Users isAdmin(@Param("email") String email);

}
