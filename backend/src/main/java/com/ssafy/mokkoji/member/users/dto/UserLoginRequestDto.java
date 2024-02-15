package com.ssafy.mokkoji.member.users.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String userId;
    private String password;
}
