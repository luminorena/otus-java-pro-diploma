package ru.otus.ms.core.updates.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaDeleteUpdateSendDto {
    @Schema(
            description = "Статус обновления",
            example = "DELETED",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "String"
    )
    private final String status = "DELETED";
    @Schema(
            description = "Идентификатор обновления",
            example = "69e95098-bd7d-4a05-b89c-f4b9c7fb151c",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "UUID"
    )
    private UUID id;
    @Schema(
            description = "Время удаления обновления",
            example = "2025-04-02T14:01:49.492289600Z",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "DateTime UTC"
    )
    private Instant deletedAt = Instant.now();


}
