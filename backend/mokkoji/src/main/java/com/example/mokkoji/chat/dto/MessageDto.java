package com.example.mokkoji.chat.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    String sessionId;
    String userName;
    String content;
    String time;
}