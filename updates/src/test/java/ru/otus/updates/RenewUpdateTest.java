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
import ru.otus.ms.core.updates.kafka.dtos.KafkaChangeUpdateSendDto;
import ru.otus.ms.core.updates.services.updates.UpdatesKafkaServiceImpl;
import ru.otus.ms.core.updates.services.updates.UpdatesRestServiceImpl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RenewUpdateTest {
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


    private UpdatesCreateRqDto createUpdatesCreateRqDto(UUID uuid) {
        return new UpdatesCreateRqDto(
                uuid,
                "DriverTest",
                "Ryzen Firmware",
                Instant.parse("2025-04-10T16:35:32.763504Z"),
                Instant.parse("2025-04-10T16:35:32.763504Z")
        );
    }

    private KafkaChangeUpdateSendDto createKafkaChangeUpdateSendDto(UUID uuid) {
        return new KafkaChangeUpdateSendDto(
                uuid,
                Instant.parse("2025-04-10T16:35:32.763504Z"),
                "Test place",
                "Test description"
        );
    }


    @DisplayName("Проверка, что метод /update/{uuid} вызывается со всеми переданными параметрами")
    @Test
    void getSuccessfulInvokeTest() {
        UUID testUuid = UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9");
        UpdatesCreateRqDto updatesCreateRqDto = createUpdatesCreateRqDto(testUuid);

        doNothing().when(updatesRestServiceImpl).renewUpdate(testUuid, updatesCreateRqDto);

        updatesController.renewUpdate(updatesCreateRqDto, testUuid);

        Mockito.verify(updatesRestServiceImpl).renewUpdate(testUuid, updatesCreateRqDto);
    }

    @DisplayName("Проверка, что метод /update/{uuid} возвращает статус код 200")
    @Test
    void getSuccessfulStatusCodeTest() {
        UUID testUuid = UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9");
        UpdatesCreateRqDto updatesCreateRqDto = createUpdatesCreateRqDto(testUuid);
        KafkaChangeUpdateSendDto kafkaChangeUpdateSendDto = createKafkaChangeUpdateSendDto(testUuid);

        doNothing().when(updatesRestServiceImpl).renewUpdate(testUuid, updatesCreateRqDto);
        when(updatesKafkaServiceImpl.getKafkaChangeUpdate(testUuid)).thenReturn(kafkaChangeUpdateSendDto);
        when(updatesKafkaServiceImpl.processChangeUpdateEntry(kafkaChangeUpdateSendDto))
                .thenReturn(Optional.of(kafkaChangeUpdateSendDto));

        ResponseEntity<Optional<KafkaChangeUpdateSendDto>> responseEntity =
                updatesController.renewUpdate(updatesCreateRqDto, testUuid);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @DisplayName("Проверка, что метод /update/{uuid} возвращает корректные данные массива data")
    @Test
    void getVerifiedDataTest() {
        UUID testUuid = UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9");
        UpdatesCreateRqDto updatesCreateRqDto = createUpdatesCreateRqDto(testUuid);
        KafkaChangeUpdateSendDto kafkaChangeUpdateSendDto = createKafkaChangeUpdateSendDto(testUuid);

        doNothing().when(updatesRestServiceImpl).renewUpdate(testUuid, updatesCreateRqDto);
        when(updatesKafkaServiceImpl.getKafkaChangeUpdate(testUuid)).thenReturn(kafkaChangeUpdateSendDto);
        when(updatesKafkaServiceImpl.processChangeUpdateEntry(kafkaChangeUpdateSendDto))
                .thenReturn(Optional.of(kafkaChangeUpdateSendDto));

        ResponseEntity<Optional<KafkaChangeUpdateSendDto>> responseEntity =
                updatesController.renewUpdate(updatesCreateRqDto, testUuid);

        Optional<KafkaChangeUpdateSendDto> body = responseEntity.getBody();

        assertEquals("Test place", body.orElseThrow().getPlace());
        assertEquals("Test description", body.get().getDescription());
    }

}
