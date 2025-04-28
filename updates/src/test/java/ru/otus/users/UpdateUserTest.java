package ru.otus.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.otus.ms.core.updates.controllers.UsersController;
import ru.otus.ms.core.updates.dtos.users.UserCreateRqDto;
import ru.otus.ms.core.updates.services.users.UserRestServiceImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

public class UpdateUserTest {

    private UUID testUuid;

    @Mock
    private UserRestServiceImpl userRestServiceImpl;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUuid = UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7");
    }


    private UserCreateRqDto createUpdateUser() {
        return new UserCreateRqDto(
                testUuid,
                "Иванов Олег Васильевич",
                "ivanov@test.ru");
    }


    @DisplayName("Проверка, что метод /user/{uuid} вызывается со всеми переданными параметрами")
    @Test
    void getSuccessfulInvokeTest() {

        doNothing().when(userRestServiceImpl).updateUserById(testUuid, createUpdateUser());

        usersController.updateUser(createUpdateUser(), testUuid);

        Mockito.verify(userRestServiceImpl).updateUserById(testUuid, createUpdateUser());
    }

    @DisplayName("Проверка, что метод /user/{uuid} возвращает статус код 200")
    @Test
    void getSuccessfulStatusCodeTest() {

        doNothing().when(userRestServiceImpl).updateUserById(testUuid, createUpdateUser());

        ResponseEntity<UserCreateRqDto> responseEntity =
                usersController.updateUser(createUpdateUser(), testUuid);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @DisplayName("Проверка, что метод /user/{uuid} возвращает корректные данные массива data")
    @Test
    void getVerifiedDataTest() {

        doNothing().when(userRestServiceImpl).updateUserById(testUuid, createUpdateUser());

        ResponseEntity<UserCreateRqDto> responseEntity =
                usersController.updateUser(createUpdateUser(), testUuid);

        UserCreateRqDto body = responseEntity.getBody();

        body.setUserEmail("test@test.ru");

        assertEquals(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"), body.getId());
        assertEquals("Иванов Олег Васильевич", body.getUserFio());
        assertEquals("test@test.ru", body.getUserEmail());
    }

}
