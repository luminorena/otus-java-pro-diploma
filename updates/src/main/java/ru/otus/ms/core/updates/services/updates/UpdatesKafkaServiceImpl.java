package ru.otus.ms.core.updates.services.updates;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.ms.core.updates.kafka.UpdatesProducer;
import ru.otus.ms.core.updates.kafka.dtos.KafkaChangeUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaCreateUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaDeleteUpdateSendDto;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdatesKafkaServiceImpl implements UpdatesKafkaService {

    private final UpdatesProducer updatesProducer;
    private final UpdatesRestServiceImpl updatesRestServiceImpl;


    @Override
    public KafkaCreateUpdateSendDto processCreatedEntry(KafkaCreateUpdateSendDto kafkaCreateUpdateSendDto) {
        return updatesProducer.sendCreateState(kafkaCreateUpdateSendDto);
    }

    @Override
    public Optional<KafkaChangeUpdateSendDto> processChangeUpdateEntry(KafkaChangeUpdateSendDto kafkaChangeUpdateSendDto) {
        return Optional.of(updatesProducer.sendChangeState(kafkaChangeUpdateSendDto));
    }

    public Optional<KafkaDeleteUpdateSendDto> processDeleteUpdateEntry(KafkaDeleteUpdateSendDto kafkaDeleteUpdateSendDto) {
        return Optional.of(updatesProducer.sendDeleteState(kafkaDeleteUpdateSendDto));
    }

    @Override
    public KafkaCreateUpdateSendDto getKafkaUpdate(UUID id) {
        return new KafkaCreateUpdateSendDto(updatesRestServiceImpl.getUpdateById(id).orElseThrow().getId(), Instant.now()
                , updatesRestServiceImpl.getUpdateById(id).orElseThrow().getPlace()
                , updatesRestServiceImpl.getUpdateById(id).orElseThrow().getDescription());

    }

    @Override
    public KafkaChangeUpdateSendDto getKafkaChangeUpdate(UUID id) {
        return new KafkaChangeUpdateSendDto(updatesRestServiceImpl.getUpdateById(id).orElseThrow().getId(), Instant.now()
                , updatesRestServiceImpl.getUpdateById(id).orElseThrow().getPlace()
                , updatesRestServiceImpl.getUpdateById(id).orElseThrow().getDescription());
    }

    @Override
    public KafkaDeleteUpdateSendDto getKafkaDeleteUpdate(UUID id) {
        return new KafkaDeleteUpdateSendDto(updatesRestServiceImpl.findDeletedUpdate(id).orElseThrow().getId(), Instant.now());
    }


}
