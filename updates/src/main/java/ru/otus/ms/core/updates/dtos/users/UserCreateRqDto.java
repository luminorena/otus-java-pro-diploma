package ru.otus.ms.core.updates.dtos.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "Пользователи")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCreateRqDto {
    @Schema(
            description = "Идентификатор пользователя, заполняется автоматически",
            example = "69e95098-bd7d-4a05-b89c-f4b9c7fb151c",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "UUID"
    )
    private UUID id = UUID.randomUUID();
    @Schema(
            description = "ФИО пользователя",
            example = "Иванов Иван Иванович",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 255
    )
    private String userFio;
    @Schema(
            description = "email пользователя",
            example = "mail@test.ru",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 120
    )
    private String userEmail;
}
