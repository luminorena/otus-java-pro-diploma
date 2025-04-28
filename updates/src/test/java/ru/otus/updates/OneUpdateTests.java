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
import ru.otus.ms.core.updates.dtos.updates.UpdatesDto;
import ru.otus.ms.core.updates.services.updates.UpdatesRestServiceImpl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OneUpdateTests {
    @Mock
    private UpdatesRestServiceImpl updatesRestServiceImpl;

    @InjectMocks
    private UpdatesController updatesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    private UpdatesDto getUpdates() {
        return new UpdatesDto(
                UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"),
                "Driver",
                "HP - USB",
                Instant.parse("2025-04-10T16:35:32.763504Z"),
                Instant.parse("2025-04-10T16:35:32.763504Z")
        );
    }


    @DisplayName("Проверка, что метод /update/{uuid} возвращает статус код 200")
    @Test
    void getSuccessfulStatusCodeTest() {

        when(updatesRestServiceImpl.getUpdateById(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9")))
                .thenReturn(Optional.of(getUpdates()));

        ResponseEntity<UpdatesDto> responseEntity = updatesController
                .getUpdateById(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @DisplayName("Проверка, что метод /update/{uuid} вызывается со всеми переданными параметрами")
    @Test
    void getSuccessfulInvokeTest() {

        when(updatesRestServiceImpl.getUpdateById(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9")))
                .thenReturn(Optional.of(getUpdates()));

        updatesController.getUpdateById(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"));

        verify(updatesRestServiceImpl).getUpdateById(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"));
    }

    @DisplayName("Проверка, что метод /update/{uuid} возвращает корректные данные")
    @Test
    void getVerifiedDataArrayTest() {

        when(updatesRestServiceImpl.getUpdateById(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9")))
                .thenReturn(Optional.of(getUpdates()));

        ResponseEntity<UpdatesDto> responseEntity =
                updatesController.getUpdateById(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"));


        Assertions.assertEquals(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"), responseEntity.getBody().getId());
        Assertions.assertEquals("Driver", responseEntity.getBody().getPlace());
        Assertions.assertEquals("HP - USB", responseEntity.getBody().getDescription());
        Assertions.assertEquals(Instant.parse("2025-04-10T16:35:32.763504Z"), responseEntity.getBody().getCreatedAt());
        Assertions.assertEquals(Instant.parse("2025-04-10T16:35:32.763504Z"), responseEntity.getBody().getUpdatedAt());
    }
}
