package ru.otus.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.otus.ms.core.updates.controllers.UsersController;
import ru.otus.ms.core.updates.dtos.users.UsersDto;
import ru.otus.ms.core.updates.services.users.UserRestServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OneUserTests {

    @Mock
    private UserRestServiceImpl userRestServiceImpl;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    private UsersDto getOneUser() {
        return new UsersDto(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"),
                "Иванов Олег Васильевич",
                "ivanov@test.ru");
    }

    @DisplayName("Проверка, что метод /user/{uuid} возвращает статус код 200")
    @Test
    void getSuccessfulStatusCodeTest() {

        when(userRestServiceImpl.getUserById(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7")))
                .thenReturn(Optional.of(getOneUser()));

        ResponseEntity<UsersDto> responseEntity = usersController
                .getUserById(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @DisplayName("Проверка, что метод /user/{uuid} вызывается со всеми переданными параметрами")
    @Test
    void getSuccessfulInvokeTest() {

        when(userRestServiceImpl.getUserById(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7")))
                .thenReturn(Optional.of(getOneUser()));

        usersController.getUserById(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"));

        verify(userRestServiceImpl).getUserById(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"));
    }

    @DisplayName("Проверка, что метод /user/{uuid} возвращает корректные данные")
    @Test
    void getVerifiedDataArrayTest() {

        when(userRestServiceImpl.getUserById(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7")))
                .thenReturn(Optional.of(getOneUser()));

        ResponseEntity<UsersDto> responseEntity =
                usersController.getUserById(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"));


        Assertions.assertEquals(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"), responseEntity.getBody().getId());
        Assertions.assertEquals("Иванов Олег Васильевич", responseEntity.getBody().getUserFio());
        Assertions.assertEquals("ivanov@test.ru", responseEntity.getBody().getUserEmail());
    }
}
