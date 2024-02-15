package com.ssafy.mokkoji.common.config.kafka;

import com.ssafy.mokkoji.common.chat.domain.response.MessageResponse;
import com.ssafy.mokkoji.common.properties.KafkaProperties;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfiguration {
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageResponse> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaListenerConsumer());
//        factory.createContainer();
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MessageResponse> kafkaListenerConsumer() {
        //        producer와 consumer에 사용될 객체의 패키지 명이 다를 경우 해당 구문 살려야함
        //        JsonDeserializer<MessageDto> deserialize = new JsonDeserializer<>();
        //        deserialize.addTrustedPackages("*");

        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer())
                        .put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId())
//                        return 에서 클래스를 반환하므로 필요없음
//                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
//                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserialize.getClass())
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getOffset())
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), new JsonDeserializer<>(MessageResponse.class));
    }
}