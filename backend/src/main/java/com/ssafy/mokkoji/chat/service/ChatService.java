package com.ssafy.mokkoji.chat.service;

import com.ssafy.mokkoji.chat.dto.response.MessageResponse;
import com.ssafy.mokkoji.chat.service.kafka.KafkaSend;
import com.ssafy.mokkoji.global.utils.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final KafkaSend sender;

    public void sendMessage(MessageResponse message) {
        sender.send(ConstantUtil.KAFKA_GAME_TOPIC, message);
    }
}