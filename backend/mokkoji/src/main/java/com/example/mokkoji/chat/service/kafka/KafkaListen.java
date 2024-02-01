package com.example.mokkoji.chat.service.kafka;

import com.example.mokkoji.chat.dto.MessageDto;
import com.example.mokkoji.chat.util.ConstantUtil;
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
    public void gameEvent(MessageDto messageDto) {
        log.info("kafka event = {}", messageDto);
        template.convertAndSend("/topic/" + messageDto.getSessionId(), messageDto);
    }
}