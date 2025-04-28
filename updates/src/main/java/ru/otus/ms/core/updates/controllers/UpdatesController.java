package ru.otus.ms.core.updates.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.ms.core.updates.dtos.updates.UpdatesCreateRqDto;
import ru.otus.ms.core.updates.dtos.updates.UpdatesDto;
import ru.otus.ms.core.updates.dtos.users.UsersDto;
import ru.otus.ms.core.updates.entities.Updates;
import ru.otus.ms.core.updates.exception_handlers.exceptions.NoResourceFoundException;
import ru.otus.ms.core.updates.kafka.dtos.KafkaChangeUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaCreateUpdateSendDto;
import ru.otus.ms.core.updates.kafka.dtos.KafkaDeleteUpdateSendDto;
import ru.otus.ms.core.updates.services.updates.UpdatesKafkaServiceImpl;
import ru.otus.ms.core.updates.services.updates.UpdatesRestServiceImpl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/updates")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Обновления", description = "Методы работы с обновлениями")
public class UpdatesController {
    private final UpdatesRestServiceImpl updatesServiceImpl;
    private final UpdatesKafkaServiceImpl updatesKafkaServiceImpl;

    @GetMapping("/all")
    @Operation(
            summary = "Запрос на вывод всех обновлений",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersDto.class))
                    )
            }
    )
    public ResponseEntity<Map<String, Object>> getAllUpdates(
            @Parameter(description = "Страница", required = true, schema = @Schema(type = "string", example = "1"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Количество элементов", required = true, schema = @Schema(type = "string", example = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Сортировка по полю", required = true, schema = @Schema(type = "string", example = "place"))
            @RequestParam(defaultValue = "place") String sortBy) {

        Page<Updates> pageResult = updatesServiceImpl.getAllUpdates(page, size, sortBy);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", pageResult.getContent());

        Map<String, Object> paging = new LinkedHashMap<>();
        paging.put("totalItems", pageResult.getTotalElements());
        paging.put("totalPages", pageResult.getTotalPages());
        paging.put("currentPage", pageResult.getNumber() + 1);
        response.put("paging", paging);
        log.info("GET /all - Retrieved {} updates from DB with pagination params: page={}, size={}, sortBy={}",
                pageResult.getTotalElements(), page, size, sortBy);
        log.debug("GET /all {} ", response.get("data"));

        return ResponseEntity.ok(response);

    }

    @GetMapping("/update/{uuid}")
    @Operation(
            summary = "Запрос на вывод данных по одному обновлению",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatesDto.class))
                    )
                    ,
                    @ApiResponse(
                            description = "Указанное обновление не найдено", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoResourceFoundException.class))
                    )
            }
    )
    public ResponseEntity<UpdatesDto> getUpdateById(
            @Parameter(description = "id обновления", required = true, schema = @Schema(type = "UUID", example = "9c93cba2-7508-4694-91b1-c9c4488f6c1d"))
            @PathVariable UUID uuid) {
        UpdatesDto updatesDto = updatesServiceImpl.getUpdateById(uuid)
                .orElseThrow(() -> new NoResourceFoundException("Update with UUID " + uuid + " not found"));
        log.info("GET updates/{uuid} {}", updatesDto);
        return ResponseEntity.ok(updatesDto);
    }


    @PostMapping("/add")
    @Operation(
            summary = "Запрос на добавление нового обновления",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление нового обновления", required = true,
                    content = @Content(schema = @Schema(implementation = UpdatesCreateRqDto.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatesCreateRqDto.class))
                    )
            }
    )
    public ResponseEntity<KafkaCreateUpdateSendDto> createNewUpdate(
            @Valid @RequestBody UpdatesCreateRqDto updatesCreateRqDto) {
        UpdatesCreateRqDto updates = updatesServiceImpl.createNewUpdate(updatesCreateRqDto);
        log.info("POST updates/ {}", updates);
        return ResponseEntity.ok().body(updatesKafkaServiceImpl.processCreatedEntry(updatesKafkaServiceImpl.getKafkaUpdate(updates.getId())));

    }


    @DeleteMapping("/update/{uuid}")
    @Operation(
            summary = "Запрос на удаление обновления",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatesDto.class))
                    ),
                    @ApiResponse(
                            description = "Обновление с указанным UUID не найдено", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatesDto.class))
                    )
            }
    )
    public ResponseEntity<Optional<KafkaDeleteUpdateSendDto>> deleteUpdateById(
            @Parameter(description = "id обновления", required = true, schema = @Schema(type = "UUID", example = "9c93cba2-7508-4694-91b1-c9c4488f6c1d"))
            @PathVariable UUID uuid) {
        if (updatesServiceImpl.deleteUpdate(uuid)) {
            log.info("DELETE /update/{uuid} {}", "update with UUID " + uuid + " is deleted");
            return ResponseEntity.ok().body(updatesKafkaServiceImpl.processDeleteUpdateEntry(updatesKafkaServiceImpl.getKafkaDeleteUpdate(uuid)));

        } else throw new NoResourceFoundException("Update with UUID " + uuid + " not found");
    }


    @PutMapping("/update/{uuid}")
    @Operation(
            summary = "Запрос на обновления данных об обновлении",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновление данных", required = true,
                    content = @Content(schema = @Schema(implementation = UpdatesCreateRqDto.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatesCreateRqDto.class))
                    )
            }
    )
    public ResponseEntity<Optional<KafkaChangeUpdateSendDto>> renewUpdate(
            @Valid @RequestBody UpdatesCreateRqDto updatesCreateRqDto,
            @Parameter(description = "id обновления", required = true, schema = @Schema(type = "string", format = "uuid", example = "9c93cba2-7508-4694-91b1-c9c4488f6c1d"))
            @PathVariable UUID uuid) {
        updatesServiceImpl.renewUpdate(uuid, updatesCreateRqDto);
        log.info("PUT /update/{uuid} {}", updatesCreateRqDto);
        return ResponseEntity.ok().body(updatesKafkaServiceImpl.processChangeUpdateEntry(updatesKafkaServiceImpl.getKafkaChangeUpdate(uuid)));
    }

}
