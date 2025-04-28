package ru.otus.updates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.otus.ms.core.updates.controllers.UpdatesController;
import ru.otus.ms.core.updates.exception_handlers.exceptions.NoResourceFoundException;
import ru.otus.ms.core.updates.kafka.dtos.KafkaDeleteUpdateSendDto;
import ru.otus.ms.core.updates.services.updates.UpdatesKafkaServiceImpl;
import ru.otus.ms.core.updates.services.updates.UpdatesRestServiceImpl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeleteUpdateTests {

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

    private KafkaDeleteUpdateSendDto kafkaDeleteUpdateSendDto() {
        return new KafkaDeleteUpdateSendDto(
                UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"),
                Instant.parse("2025-04-10T16:35:32.763504Z")
        );
    }

    @DisplayName("Проверка, что метод /update/{uuid} возвращает статус код 200 и нужную DTO")
    @Test
    void getSuccessfulDeletedUpdate() {

        UUID uuid = UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9");

        when(updatesRestServiceImpl.deleteUpdate(uuid)).thenReturn(true);

        when(updatesKafkaServiceImpl.getKafkaDeleteUpdate(uuid)).thenReturn(kafkaDeleteUpdateSendDto());

        when(updatesKafkaServiceImpl.processDeleteUpdateEntry(kafkaDeleteUpdateSendDto()))
                .thenReturn(Optional.of(kafkaDeleteUpdateSendDto()));

        ResponseEntity<Optional<KafkaDeleteUpdateSendDto>> response = updatesController.deleteUpdateById(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(updatesRestServiceImpl).deleteUpdate(uuid);
        verify(updatesKafkaServiceImpl).getKafkaDeleteUpdate(uuid);
        verify(updatesKafkaServiceImpl).processDeleteUpdateEntry(kafkaDeleteUpdateSendDto());
    }

    @DisplayName("Проверка, что метод /update/{uuid} возвращает статус код 404 и нужную DTO")
    @Test
    void getExceptionAfterDeletion() {

        UUID uuid = UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9");

        when(updatesRestServiceImpl.deleteUpdate(uuid)).thenReturn(false);

        NoResourceFoundException exception = Assertions.assertThrows(NoResourceFoundException.class, () -> {
            updatesController.deleteUpdateById(uuid);
        });

        assertEquals("Update with UUID " + uuid + " not found", exception.getMessage());

        verify(updatesRestServiceImpl).deleteUpdate(uuid);


    }


}
