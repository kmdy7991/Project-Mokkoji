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
        ENTER, START, CHAT, THEME, SUCCESS;
    }
}
// 일단 주제는 테마로 보내고 성공햇는지는 채팅에 보낼지 성공에 보낼지 생각해보기 일단 그 알고리즘 find에서 무조건 사용해야함