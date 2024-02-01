package com.example.mokkoji.member.api.repository.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    private String userId;
    private String nickname;
}