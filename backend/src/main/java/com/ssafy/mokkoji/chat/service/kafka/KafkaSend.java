package com.ssafy.mokkoji.chat.service.kafka;

import com.ssafy.mokkoji.chat.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSend {
    private final KafkaTemplate<String, MessageResponse> kafkaTemplate;

    public void send(String topic, MessageResponse message){
        log.info("kafka publish = {}, {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
