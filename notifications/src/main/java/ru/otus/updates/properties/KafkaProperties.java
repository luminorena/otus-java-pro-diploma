package ru.otus.updates.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "updates.kafka")
@Data
public class KafkaProperties {
    String brokers;
    String topic;
    String consumerGroup;
}
