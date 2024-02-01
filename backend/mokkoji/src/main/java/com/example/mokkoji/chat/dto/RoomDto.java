package com.example.mokkoji.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoomDto {
    String roomId;
    String roomName;
}
