package com.ssafy.mokkoji.member.users.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    private String email;
    private String nickname;
}