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
import static org.mockito.Mockito.when;

public class CreateNewUserTests {

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


    @DisplayName("Проверка, что метод /add возвращает статус код 200 и нужную DTO")
    @Test
    void getNewUserTest() {

        when(userRestServiceImpl.createNewUser(createUpdateUser()))
                .thenReturn(createUpdateUser());

        ResponseEntity<UserCreateRqDto> responseEntity = usersController.createNewUser(createUpdateUser());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertEquals(createUpdateUser(), responseEntity.getBody());

        Mockito.verify(userRestServiceImpl).createNewUser(createUpdateUser());
    }


}
