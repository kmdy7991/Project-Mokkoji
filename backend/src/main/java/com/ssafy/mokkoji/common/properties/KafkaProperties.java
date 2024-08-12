package com.ssafy.mokkoji.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String groupId;
    private String server;
    private String offset;
}
