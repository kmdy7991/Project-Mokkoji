package com.ssafy.mokkoji.common.chat.service.kafka;

import com.ssafy.mokkoji.common.utils.ConstantUtil;
import com.ssafy.mokkoji.common.chat.domain.response.MessageResponse;
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

    @KafkaListener(topics = ConstantUtil.KAFKA_GAME_TOPIC, containerFactory = "kafkaListenerContainerFactory", concurrency = "3")
    public void gameEvent(MessageResponse message) {
        log.info("kafka event = {}", message);
        template.convertAndSend("/topic/" + message.getRoomId(), message);
    }
}