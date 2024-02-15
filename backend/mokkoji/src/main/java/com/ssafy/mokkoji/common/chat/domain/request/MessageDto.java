package com.ssafy.mokkoji.common.chat.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
public class MessageDto {
    private String roomId;
    @JsonProperty("user_nickname")
    private String userNickname;
    private String content;
    private Type type;
    public enum Type {
        ENTER, START, CHAT, THEME;
    }
}