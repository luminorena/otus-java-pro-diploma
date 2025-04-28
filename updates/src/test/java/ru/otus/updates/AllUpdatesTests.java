package ru.otus.updates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.otus.ms.core.updates.controllers.UpdatesController;
import ru.otus.ms.core.updates.entities.Updates;
import ru.otus.ms.core.updates.services.updates.UpdatesRestServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class AllUpdatesTests {

    private int page;
    private int size;
    private String sortBy;

    @Mock
    private UpdatesRestServiceImpl updatesRestServiceImpl;

    @InjectMocks
    private UpdatesController updatesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        page = 0;
        size = 10;
        sortBy = "place";
    }


    @DisplayName("Проверка, что метод /updates/all возвращает статус код 200")
    @Test
    void getSuccessfulStatusCodeTest() {

        Page<Updates> pageResult = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(page, size, Sort.by(sortBy)),
                0);

        when(updatesRestServiceImpl.getAllUpdates(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = updatesController.getAllUpdates(page, size, sortBy);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @DisplayName("Проверка, что метод /updates/all вызывается со всеми переданными параметрами")
    @Test
    void getSuccessfulInvokeTest() {

        Page<Updates> pageResult = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(page, size, Sort.by(sortBy)),
                0);

        when(updatesRestServiceImpl.getAllUpdates(page, size, sortBy)).thenReturn(pageResult);

        updatesController.getAllUpdates(page, size, sortBy);

        verify(updatesRestServiceImpl).getAllUpdates(page, size, sortBy);
    }


    @DisplayName("Проверка, что метод /updates/all возвращает возвращает массив data и paging")
    @Test
    void getPredefinedStructureTest() {
        Page<Updates> pageResult = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(page, size, Sort.by(sortBy)),
                0);

        when(updatesRestServiceImpl.getAllUpdates(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = updatesController.getAllUpdates(page, size, sortBy);

        Map<String, Object> responseBody = responseEntity.getBody();

        Assertions.assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("data"));
        assertTrue(responseBody.containsKey("paging"));
    }

    @DisplayName("Проверка, что метод /updates/all возвращает корректные данные массива data")
    @Test
    void getVerifiedDataArrayTest() {

        Updates updates = new Updates(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"),
                "Driver",
                "HP - USB",
                Instant.parse("2025-04-10T16:35:32.763504Z"),
                Instant.parse("2025-04-10T16:35:32.763504Z"));

        List<Updates> updatesList = Collections.singletonList(updates);
        Page<Updates> pageResult = new PageImpl<>(updatesList,
                PageRequest.of(page, size, Sort.by(sortBy)),
                1);

        when(updatesRestServiceImpl.getAllUpdates(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = updatesController.getAllUpdates(page, size, sortBy);

        List<Updates> responseUpdates = (List<Updates>) responseEntity.getBody().get("data");

        Assertions.assertEquals(1, responseUpdates.size());
        Assertions.assertEquals(UUID.fromString("dbeef044-e349-4568-98fd-dcc32b0907c9"), responseUpdates.get(0).getId());
        Assertions.assertEquals("Driver", responseUpdates.get(0).getPlace());
        Assertions.assertEquals("HP - USB", responseUpdates.get(0).getDescription());
        Assertions.assertEquals(Instant.parse("2025-04-10T16:35:32.763504Z"), responseUpdates.get(0).getCreatedAt());
        Assertions.assertEquals(Instant.parse("2025-04-10T16:35:32.763504Z"), responseUpdates.get(0).getUpdatedAt());
    }

    @DisplayName("Проверка, что метод /updates/all возвращает корректные данные пагинации")
    @Test
    void getVerifiedPagingDataTest() {

        Updates updates = new Updates(UUID.randomUUID(),
                "Driver",
                "HP - USB",
                Instant.now(),
                Instant.now());

        List<Updates> updatesList = Collections.singletonList(updates);
        Page<Updates> pageResult = new PageImpl<>(updatesList,
                PageRequest.of(page, size, Sort.by(sortBy)),
                1);

        when(updatesRestServiceImpl.getAllUpdates(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = updatesController.getAllUpdates(page, size, sortBy);

        Map<String, Object> paging = (Map<String, Object>) responseEntity.getBody().get("paging");

        Assertions.assertEquals(1L, paging.get("totalItems"));
        Assertions.assertEquals(1, paging.get("totalPages"));
        Assertions.assertEquals(1, paging.get("currentPage"));
    }

}
