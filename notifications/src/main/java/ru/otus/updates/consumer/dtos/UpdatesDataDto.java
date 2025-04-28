package ru.otus.updates.consumer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpdatesDataDto {
    private UUID id;
    private String place;
    private String description;
}
