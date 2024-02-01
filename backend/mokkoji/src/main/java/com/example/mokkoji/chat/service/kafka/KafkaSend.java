package com.example.mokkoji.chat.service.kafka;

import com.example.mokkoji.chat.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSend {
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    public void send(String topic, MessageDto messageDto){
        log.info("kafka publish = {}, {}", topic, messageDto);
        kafkaTemplate.send(topic, messageDto);
    }
}
