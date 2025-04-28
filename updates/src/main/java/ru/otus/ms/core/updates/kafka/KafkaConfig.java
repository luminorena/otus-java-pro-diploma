package ru.otus.ms.core.updates.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.otus.ms.core.updates.kafka.properties.KafkaProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;

    public KafkaConfig(ObjectMapper objectMapper, KafkaProperties kafkaProperties) {
        this.objectMapper = objectMapper;
        this.kafkaProperties = kafkaProperties;
    }


    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put("bootstrap.servers", kafkaProperties.getBrokers());
        configProps.put("key.serializer", StringSerializer.class);

        JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(objectMapper);

        DefaultKafkaProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(configProps);
        factory.setValueSerializer(jsonSerializer);

        return factory;
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
