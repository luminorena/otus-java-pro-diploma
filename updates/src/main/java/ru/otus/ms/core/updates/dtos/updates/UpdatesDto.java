package ru.otus.ms.core.updates.dtos.updates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Schema(description = "Обновления")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpdatesDto {
    @Schema(
            description = "Идентификатор обновления",
            example = "69e95098-bd7d-4a05-b89c-f4b9c7fb151c",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "UUID"
    )
    private UUID id;
    @Schema(
            description = "Затрагивающая область",
            example = "Driver",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    private String place;
    @Schema(
            description = "Описание обновления",
            example = "Windows Subsystem for Linux Update 5.10.102.2",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 500
    )
    private String description;
    @Schema(
            description = "Время создания обновления",
            example = "2025-03-16 21:06:00.053483",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "DateTime UTC"
    )
    private Instant createdAt;
    @Schema(
            description = "Время, когда обновление было обновлено",
            example = "2025-03-16 21:06:00.053483",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "DateTime UTC"
    )
    private Instant updatedAt;
}
