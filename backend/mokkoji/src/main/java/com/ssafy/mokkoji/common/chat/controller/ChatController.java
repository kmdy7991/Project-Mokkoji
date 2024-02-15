package com.ssafy.mokkoji.common.chat.controller;

import com.ssafy.mokkoji.common.chat.domain.request.MessageDto;
import com.ssafy.mokkoji.common.chat.domain.response.MessageResponse;
import com.ssafy.mokkoji.common.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // message 매핑으로 들어옴 /pub/message 프론트에서 보낼 때 백에서 받을 때는 /roomId
    @MessageMapping("/{roomId}")
    public void receive(MessageDto messageDto) {
        log.info("get Message {}", messageDto);
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        chatService.sendMessage(
                switch (messageDto.getType()) {
                    case ENTER -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname("시스템")
                            .content(messageDto.getUserNickname() + "님 입장하였습니다!!")
                            .type(MessageResponse.Type.ENTER)
                            .build();
                    case START -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname("시스템")
                            .content("게임을 시작합니다")
                            .type(MessageResponse.Type.START)
                            .build();
                    case CHAT -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .userNickname(messageDto.getUserNickname())
                            .content(messageDto.getContent())
                            .time(time)
                            .type(MessageResponse.Type.CHAT)
                            .build();
                    case THEME -> MessageResponse.
                            builder()
                            .roomId(messageDto.getRoomId())
                            .content("fd")
                            .type(MessageResponse.Type.THEME)
                            .build();
                }
        );
    }
}