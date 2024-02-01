package com.example.mokkoji.chat.service;

import com.example.mokkoji.chat.dto.MessageDto;
import com.example.mokkoji.chat.service.kafka.KafkaSend;
import com.example.mokkoji.chat.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final KafkaSend sender;

    // jwt token으로 채팅 보안 향상 가능
    public void sendMessage(MessageDto messageDto){
        sender.send(ConstantUtil.KAFKA_GAME_TOPIC, messageDto);
    }

}
