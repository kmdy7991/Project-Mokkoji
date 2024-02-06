package com.ssafy.mokkoji.member.users.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int isSameNickname(String nickname);

    int guestInput(String nickname);

}
