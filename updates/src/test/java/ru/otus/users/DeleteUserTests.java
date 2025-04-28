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
import ru.otus.ms.core.updates.exception_handlers.exceptions.NoResourceFoundException;
import ru.otus.ms.core.updates.services.users.UserRestServiceImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeleteUserTests {

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


    @DisplayName("Проверка, что метод /user/{uuid} возвращает статус код 200 и нужную DTO")
    @Test
    void getSuccessfulDeletedUser() {

        when(userRestServiceImpl.deleteUser(testUuid)).thenReturn(true);

        ResponseEntity<org.apache.hc.core5.http.HttpStatus> response = usersController.deleteUserById(testUuid);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(userRestServiceImpl).deleteUser(testUuid);
    }

    @DisplayName("Проверка, что метод /user/{uuid} возвращает статус код 404 и нужную DTO")
    @Test
    void getExceptionAfterDeletion() {

        when(userRestServiceImpl.deleteUser(testUuid)).thenReturn(false);

        NoResourceFoundException exception = Assertions.assertThrows(NoResourceFoundException.class, () -> {
            usersController.deleteUserById(testUuid);
        });

        assertEquals("User with UUID " + testUuid + " not found", exception.getMessage());

        verify(userRestServiceImpl).deleteUser(testUuid);


    }


}
