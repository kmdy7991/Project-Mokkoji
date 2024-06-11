package com.ssafy.mokkoji.chat.service.kafka;

import com.ssafy.mokkoji.global.utils.ConstantUtil;
import com.ssafy.mokkoji.chat.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListen {

    private final SimpMessageSendingOperations template;

    @KafkaListener(topics = ConstantUtil.KAFKA_GAME_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void gameEvent(MessageResponse message) {
        log.info("kafka event = {}", message);
        template.convertAndSend("/topic/" + message.getRoomId(), message);
    }
}