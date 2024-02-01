package com.example.mokkoji.chat.controller;

import com.example.mokkoji.chat.dto.MessageDto;
import com.example.mokkoji.chat.dto.RoomDto;
import com.example.mokkoji.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private Map<String, List<String>> chatRoom = new ConcurrentHashMap<>();

//    @PostMapping("/create")
//    public ResponseEntity<RoomDto> createCheck(@RequestBody RoomDto info) {
//        RoomDto res = RoomDto.builder()
//                .roomId(info.getRoomId())
//                .roomName(info.getRoomName())
//                .build();
//        return ResponseEntity.ok().body(res);
//    }

    // message 매핑으로 들어옴 /pub/message 프론트에서 보낼 때 백에서 받을 때는 /roomId
    @MessageMapping("/{roomId}")
    public void send(@PathVariable("roomId") String roomId, MessageDto messageDto){
        log.info("get Message {}", messageDto);

        chatService.sendMessage(messageDto);
    }
}
