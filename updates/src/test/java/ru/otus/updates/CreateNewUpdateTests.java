package ru.otus.updates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.otus.ms.core.updates.controllers.UpdatesController;
import ru.otus.ms.core.updates.dtos.updates.UpdatesCreateRqDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaCreateUpdateSendDto;
import ru.otus.ms.core.updates.services.updates.UpdatesKafkaServiceImpl;
import ru.otus.ms.core.updates.services.updates.UpdatesRestServiceImpl;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CreateNewUpdateTests {

    @Mock
    private UpdatesRestServiceImpl updatesRestServiceImpl;

    @Mock
    private UpdatesKafkaServiceImpl updatesKafkaServiceImpl;

    @InjectMocks
    private UpdatesController updatesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UpdatesCreateRqDto updatesCreateRqDto() {
        return new UpdatesCreateRqDto(
                UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"),
                "DriverTest",
                "Ryzen Firmware",
                Instant.parse("2025-04-10T16:35:32.763504Z"),
                Instant.parse("2025-04-10T16:35:32.763504Z")
        );
    }

    private KafkaCreateUpdateSendDto kafkaCreateUpdateSendDto() {
        return new KafkaCreateUpdateSendDto(
                UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"),
                Instant.parse("2025-04-10T16:35:32.763504Z"),
                "DriverTest",
                "Ryzen Firmware"
        );
    }


    @DisplayName("Проверка, что метод /add возвращает статус код 200 и нужную DTO")
    @Test
    void getNewUpdateTest() {

        when(updatesRestServiceImpl.createNewUpdate(updatesCreateRqDto()))
                .thenReturn(updatesCreateRqDto());

        when(updatesKafkaServiceImpl.getKafkaUpdate(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9")))
                .thenReturn(kafkaCreateUpdateSendDto());

        when(updatesKafkaServiceImpl.processCreatedEntry(kafkaCreateUpdateSendDto()))
                .thenReturn(kafkaCreateUpdateSendDto());

        ResponseEntity<KafkaCreateUpdateSendDto> responseEntity = updatesController.createNewUpdate(updatesCreateRqDto());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(kafkaCreateUpdateSendDto(), responseEntity.getBody());

        Mockito.verify(updatesRestServiceImpl).createNewUpdate(updatesCreateRqDto());
    }


}
