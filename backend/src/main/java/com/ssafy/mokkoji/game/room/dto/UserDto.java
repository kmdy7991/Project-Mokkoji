package com.ssafy.mokkoji.game.room.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class UserDto {
    private int id;
    private  String user_nickname;
    private String email;
    private provider provider;
    private role role;
    private String refresh_token;
    private String created_at;
    public enum provider{
        GOOGLE, NAVER, KAKAO
    }
    public enum role{
        USER, ADMIN, GUEST
    }
}
