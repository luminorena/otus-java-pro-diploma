package ru.otus.ms.core.updates.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.ms.core.updates.dtos.users.UserCreateRqDto;
import ru.otus.ms.core.updates.dtos.users.UsersDto;
import ru.otus.ms.core.updates.entities.Users;
import ru.otus.ms.core.updates.exception_handlers.exceptions.NoResourceFoundException;
import ru.otus.ms.core.updates.services.users.UserRestService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Пользователи", description = "Методы работы с пользователями")
public class UsersController {

    private final UserRestService userRestService;

    @SneakyThrows
    @GetMapping("/all")
    @Operation(
            summary = "Запрос на вывод всех пользователей",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersDto.class))
                    )
            }
    )
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @Parameter(description = "Страница", required = true, schema = @Schema(type = "string", example = "1"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Количество элементов", required = true, schema = @Schema(type = "string", example = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Сортировка по полю", required = true, schema = @Schema(type = "string", example = "userFio"))
            @RequestParam(defaultValue = "userFio") String sortBy) {

        Page<Users> pageResult = userRestService.getAllUsers(page, size, sortBy);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", pageResult.getContent());

        Map<String, Object> paging = new LinkedHashMap<>();
        paging.put("totalItems", pageResult.getTotalElements());
        paging.put("totalPages", pageResult.getTotalPages());
        paging.put("currentPage", pageResult.getNumber() + 1);
        response.put("paging", paging);
        log.info("GET /all - Retrieved {} users from DB with pagination params: page={}, size={}, sortBy={}",
                pageResult.getTotalElements(), page, size, sortBy);
        log.debug("GET /all {} ", response.get("data"));

        return ResponseEntity.ok(response);

    }

    @GetMapping("/user/{uuid}")
    @Operation(
            summary = "Запрос на вывод данных по одному пользователю",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersDto.class))
                    )
                    ,
                    @ApiResponse(
                            description = "Указанный пользователь не найден", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoResourceFoundException.class))
                    )
            }
    )
    public ResponseEntity<UsersDto> getUserById(
            @Parameter(description = "id пользователя", required = true, schema = @Schema(type = "UUID", example = "9c93cba2-7508-4694-91b1-c9c4488f6c1d"))
            @PathVariable UUID uuid) {
        UsersDto usersDto = userRestService.getUserById(uuid)
                .orElseThrow(() -> new NoResourceFoundException("User with UUID " + uuid + " not found"));
        log.info("GET users/{uuid} {}", usersDto);
        return ResponseEntity.ok(usersDto);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Запрос на создание пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Создание пользователя", required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRqDto.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCreateRqDto.class))
                    )
            }
    )
    public ResponseEntity<UserCreateRqDto> createNewUser(
            @Valid @RequestBody UserCreateRqDto userCreateRqDto) {
        UserCreateRqDto newUser = userRestService.createNewUser(userCreateRqDto);
        log.info("POST /users {}", newUser);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(newUser);
    }


    @DeleteMapping("/user/{uuid}")
    @Operation(
            summary = "Запрос на удаление пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersDto.class))
                    ),
                    @ApiResponse(
                            description = "Пользователь с указанным UUID не найден", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersDto.class))
                    )
            }
    )
    public ResponseEntity<HttpStatus> deleteUserById(
            @Parameter(description = "id пользователя", required = true, schema = @Schema(type = "UUID", example = "9c93cba2-7508-4694-91b1-c9c4488f6c1d"))
            @PathVariable UUID uuid) {
        if (userRestService.deleteUser(uuid)) {
            log.info("DELETE /user/{uuid} {}", "user with UUID " + uuid + " is deleted");
            return ResponseEntity.noContent().build();
        } else throw new NoResourceFoundException("User with UUID " + uuid + " not found");
    }

    @PutMapping("/user/{uuid}")
    @Operation(
            summary = "Запрос на обновления данных о пользователе",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновление пользователя", required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRqDto.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCreateRqDto.class))
                    )
            }
    )
    public ResponseEntity<UserCreateRqDto> updateUser(
            @Valid @RequestBody UserCreateRqDto userCreateRqDto,
            @Parameter(description = "id пользователя", required = true, schema = @Schema(type = "string", format = "uuid", example = "9c93cba2-7508-4694-91b1-c9c4488f6c1d"))
            @PathVariable UUID uuid) {
        userRestService.updateUserById(uuid, userCreateRqDto);
        log.info("PUT /users/{uuid} {}", userCreateRqDto);
        return ResponseEntity.status(HttpStatus.SC_OK).body(userCreateRqDto);
    }


}
