package ru.otus.ms.core.updates.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.ms.core.updates.kafka.dtos.KafkaChangeUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaCreateUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaDeleteUpdateSendDto;
import ru.otus.ms.core.updates.kafka.properties.KafkaProperties;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdatesProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final KafkaProperties kafkaProperties;

    @SneakyThrows
    public KafkaCreateUpdateSendDto sendCreateState(KafkaCreateUpdateSendDto kafkaTransferDto) {
        kafkaTemplate.send(kafkaProperties.getTopic(), kafkaTransferDto).get();
        String serializedMessage = objectMapper.writeValueAsString(kafkaTransferDto);
        log.info("#### -> Produced a message {}", serializedMessage);

        return kafkaTransferDto;

    }

    @SneakyThrows
    public KafkaChangeUpdateSendDto sendChangeState(KafkaChangeUpdateSendDto kafkaChangeUpdateSendDto) {
        kafkaTemplate.send(kafkaProperties.getTopic(), kafkaChangeUpdateSendDto).get();
        String serializedMessage = objectMapper.writeValueAsString(kafkaChangeUpdateSendDto);
        log.info("#### -> Produced a message {}", serializedMessage);

        return kafkaChangeUpdateSendDto;

    }

    @SneakyThrows
    public KafkaDeleteUpdateSendDto sendDeleteState(KafkaDeleteUpdateSendDto kafkaDeleteUpdateSendDto) {
        kafkaTemplate.send(kafkaProperties.getTopic(), kafkaDeleteUpdateSendDto).get();
        String serializedMessage = objectMapper.writeValueAsString(kafkaDeleteUpdateSendDto);
        log.info("#### -> Produced a message {}", serializedMessage);

        return kafkaDeleteUpdateSendDto;

    }


}
