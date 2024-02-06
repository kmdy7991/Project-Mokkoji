package com.ssafy.mokkoji.common.chat.service;

import com.ssafy.mokkoji.common.chat.service.kafka.KafkaSend;
import com.ssafy.mokkoji.common.utils.ConstantUtil;
import com.ssafy.mokkoji.common.chat.domain.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final KafkaSend sender;

    @Override
    public void sendMessage(MessageResponse message) {
        sender.send(ConstantUtil.KAFKA_GAME_TOPIC, message);
    }

}
