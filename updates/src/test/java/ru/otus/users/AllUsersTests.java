package ru.otus.users;

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
import ru.otus.ms.core.updates.controllers.UsersController;
import ru.otus.ms.core.updates.entities.Users;
import ru.otus.ms.core.updates.services.users.UserRestServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class AllUsersTests {

    private int page;
    private int size;
    private String sortBy;

    @Mock
    private UserRestServiceImpl userRestServiceImpl;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        page = 0;
        size = 10;
        sortBy = "userFio";
    }

    private Users getOneUser() {
        return new Users(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"),
                "Иванов Олег Васильевич",
                "ivanov@test.ru");
    }


    @DisplayName("Проверка, что метод /all возвращает статус код 200")
    @Test
    void getSuccessfulStatusCodeTest() {

        Page<Users> pageResult = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(page, size, Sort.by(sortBy)),
                0);

        when(userRestServiceImpl.getAllUsers(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = usersController.getAllUsers(page, size, sortBy);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @DisplayName("Проверка, что метод /all вызывается со всеми переданными параметрами")
    @Test
    void getSuccessfulInvokeTest() {

        Page<Users> pageResult = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(page, size, Sort.by(sortBy)),
                0);

        when(userRestServiceImpl.getAllUsers(page, size, sortBy)).thenReturn(pageResult);

        usersController.getAllUsers(page, size, sortBy);

        verify(userRestServiceImpl).getAllUsers(page, size, sortBy);
    }


    @DisplayName("Проверка, что метод /all возвращает возвращает массив data и paging")
    @Test
    void getPredefinedStructureTest() {

        Page<Users> pageResult = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(page, size, Sort.by(sortBy)),
                0);

        when(userRestServiceImpl.getAllUsers(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = usersController.getAllUsers(page, size, sortBy);

        Map<String, Object> responseBody = responseEntity.getBody();

        Assertions.assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("data"));
        assertTrue(responseBody.containsKey("paging"));
    }


    @DisplayName("Проверка, что метод /all возвращает корректные данные массива data")
    @Test
    void getVerifiedDataArrayTest() {

        List<Users> usersList = Collections.singletonList(getOneUser());
        Page<Users> pageResult = new PageImpl<>(usersList,
                PageRequest.of(page, size, Sort.by(sortBy)),
                1);

        when(userRestServiceImpl.getAllUsers(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = usersController.getAllUsers(page, size, sortBy);

        List<Users> responseUpdates = (List<Users>) responseEntity.getBody().get("data");

        Assertions.assertEquals(1, responseUpdates.size());
        Assertions.assertEquals(UUID.fromString("918761b7-78a6-455f-b66a-2c9d82a856b7"), responseUpdates.get(0).getId());
        Assertions.assertEquals("Иванов Олег Васильевич", responseUpdates.get(0).getUserFio());
        Assertions.assertEquals("ivanov@test.ru", responseUpdates.get(0).getUserEmail());
    }

    @DisplayName("Проверка, что метод /all возвращает корректные данные пагинации")
    @Test
    void getVerifiedPagingDataTest() {

        List<Users> usersList = Collections.singletonList(getOneUser());
        Page<Users> pageResult = new PageImpl<>(usersList,
                PageRequest.of(page, size, Sort.by(sortBy)),
                1);

        when(userRestServiceImpl.getAllUsers(page, size, sortBy)).thenReturn(pageResult);

        ResponseEntity<Map<String, Object>> responseEntity = usersController.getAllUsers(page, size, sortBy);

        Map<String, Object> paging = (Map<String, Object>) responseEntity.getBody().get("paging");

        Assertions.assertEquals(1L, paging.get("totalItems"));
        Assertions.assertEquals(1, paging.get("totalPages"));
        Assertions.assertEquals(1, paging.get("currentPage"));
    }

}
