package com.ssafy.mokkoji.common.chat.domain.response;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse{
    private String roomId;
    private String userNickname;
    private String content;
    private String time;
    private Type type;
    public enum Type {
        ENTER, START, CHAT, THEME
    }
}
