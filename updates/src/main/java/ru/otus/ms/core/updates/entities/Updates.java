package ru.otus.ms.core.updates.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Updates {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "operation_system_area")
    private String place;
    @Column(name = "description")
    private String description;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
}
