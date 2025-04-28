package ru.otus.ms.core.updates.services.updates;

import ru.otus.ms.core.updates.kafka.dtos.KafkaChangeUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaCreateUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaDeleteUpdateSendDto;

import java.util.Optional;
import java.util.UUID;


public interface UpdatesKafkaService {
    KafkaCreateUpdateSendDto processCreatedEntry(KafkaCreateUpdateSendDto kafkaCreateUpdateSendDto);

    Optional<KafkaChangeUpdateSendDto> processChangeUpdateEntry(KafkaChangeUpdateSendDto kafkaChangeUpdateSendDto);

    KafkaCreateUpdateSendDto getKafkaUpdate(UUID id);

    KafkaChangeUpdateSendDto getKafkaChangeUpdate(UUID id);

    KafkaDeleteUpdateSendDto getKafkaDeleteUpdate(UUID id);


}
