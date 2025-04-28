package ru.otus.updates.consumer.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ConsumerDto {
    private UUID id;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deletedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String place;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
}
