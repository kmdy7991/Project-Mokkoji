package com.ssafy.mokkoji.common.chat.domain.response;

import lombok.*;

import java.util.List;
import java.util.Map;

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
    private boolean corrects = false;
    private List<Map<String, Object>> userList;
    public enum Type {
        ENTER, START, CHAT, THEME, SUCCESS, OWNER, END
    }
}
